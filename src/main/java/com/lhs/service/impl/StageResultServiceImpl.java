package com.lhs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhs.common.exception.ServiceException;
import com.lhs.common.util.ResultCode;
import com.lhs.mapper.StageResultMapper;
import com.lhs.common.util.FileUtil;
import com.lhs.mapper.StageMapper;
import com.lhs.model.entity.StageResult;
import com.lhs.common.config.FileConfig;
import com.lhs.model.entity.Item;
import com.lhs.model.entity.Stage;
import com.lhs.model.vo.PenguinDataResponseVo;
import com.lhs.service.StageResultService;
import com.lhs.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service

public class StageResultServiceImpl extends ServiceImpl<StageResultMapper, StageResult> implements StageResultService {

    @Autowired
    private StageService stageService;

    @Autowired
    private StageResultMapper stageResultMapper;

    @Override
    @Transactional
    public HashMap<String, Double> initData(List<Item> items) {

        //将企鹅物流的数据转成集合
//        String response = HttpRequestUtil.doGet("https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated");
        String response = FileUtil.read(FileConfig.Penguin + "matrix2023-02-12 20auto.json");  //读取企鹅物流数据文件
        List<PenguinDataResponseVo> penguinDataResponseVos = JSONArray.parseArray(JSONObject.parseObject(response).getString("matrix"), PenguinDataResponseVo.class);//将企鹅物流文件的内容转为集合
        penguinDataResponseVos = mergePenguinData(penguinDataResponseVos);  //合并企鹅物流的标准和磨难关卡的样本
        Map<String, Item> itemValueMap = items.stream().collect(Collectors.toMap(Item::getItemId, Function.identity())); //将item表的各项信息转为Map  <itemId,Item类 >
        Map<String, Stage> stageInfoMap = stageService.findAll().stream().collect(Collectors.toMap(Stage::getStageId, Function.identity()));  //将stage的各项信息转为Map <stageId,stage类 >


//      过滤掉（该条记录的样本低于300 & 该条记录的掉落材料不存在于材料表中 & 该条记录的关卡ID不存在于关卡表中）的数据
        penguinDataResponseVos = penguinDataResponseVos.stream()
                .filter(penguinData -> penguinData.getTimes() > 300 && itemValueMap.get(penguinData.getItemId()) != null
                        && stageInfoMap.get(penguinData.getStageId()) != null)
                .collect(Collectors.toList());


//      保存企鹅物流每一条记录的结果
        List<StageResult> stageResultList = new ArrayList<>();   //关卡效率的计算结果
        long id = new Date().getTime() * 100000;   //id为时间戳后加00001至99999
        for (PenguinDataResponseVo penguinData : penguinDataResponseVos) {
            StageResult stageResult = new StageResult();
            stageResult.setId(id++);

            Stage stage = stageInfoMap.get(penguinData.getStageId());
            Item item = itemValueMap.get(penguinData.getItemId());

            stageResult.setStageId(stage.getStageId());
            stageResult.setStageCode(stage.getStageCode());
            stageResult.setApCost(stage.getApCost());
            stageResult.setSecondary(stage.getSecondary());
            stageResult.setSecondary(stage.getSecondary());
            stageResult.setIsShow(stage.getIsShow());
            stageResult.setIsValue(stage.getIsValue());
            stageResult.setZoneName(stage.getZoneName());

            stageResult.setSpm(stage.getSpm());
            if (item.getItemName().equals(stage.getMain())) {   // 只有该关卡的主产物的计算结果才保存材料类型和材料等级
                stageResult.setMain(stage.getMain());
                stageResult.setItemType(stage.getItemType());
            }


            Double knockRating = ((double) penguinData.getQuantity() / (double) penguinData.getTimes());  //材料掉率
            stageResult.setSampleSize(penguinData.getTimes());
            stageResult.setItemId(item.getItemId());
            stageResult.setItemName(item.getItemName());
            stageResult.setKnockRating(knockRating);
            stageResult.setApExpect(stage.getApCost() / knockRating);
            stageResult.setResult(item.getItemValue() * knockRating);

            stageResult.setStageColor(2);
            stageResultList.add(stageResult);

            //前端会展示但是不用来定价的为活动本
            if (stage.getIsShow() == 1 && stage.getIsValue() == 0) {
                StageResult efficiencyResultCopy = SerializationUtils.clone(stageResult);
                efficiencyResultCopy.setId(id + 100000);
                efficiencyResultCopy.setStageId(stage.getStageId() + "_LMD");
                efficiencyResultCopy.setResult(efficiencyResultCopy.getResult() + stage.getApCost() * 0.09);
                efficiencyResultCopy.setStageColor(-1);
                stageResultList.add(efficiencyResultCopy);
            }
        }

        stageResultList.stream()
                .collect(Collectors.groupingBy(StageResult::getStageId))   //把计算结果根据stageId分组
                .forEach((stageId, list) -> {      //list是相同关卡的所有材料的单条计算结果
                    double sum = list.stream().mapToDouble(StageResult::getResult).sum();   //计算关卡的材料产出价值之和V
                    Double apCost = list.get(0).getApCost(); //拿到关卡的消耗
                    //计算效率之后保存到该关卡的每一条结果，效率公式为（V+apCost*0.0045*1.2）/apCost
                    list.forEach(result -> result.setStageEfficiency((sum + apCost * 0.054) / apCost));
                });


        //  <蓝材料名称 , 蓝材料对应的常驻最高关卡效率En>  value用于下面蓝材料的计算，
        HashMap<String, Double> itemNameAndStageEffMap = new HashMap<>();

        //将关卡效率计算结果根据材料类别分组，存入上面的 itemNameAndStageEffMap
        stageResultList.stream()
                .filter(stageResult -> stageResult.getItemType() != null && stageResult.getIsValue() == 1)
                .sorted(Comparator.comparing(StageResult::getStageEfficiency).reversed())
                .collect(Collectors.groupingBy(StageResult::getItemType))
                .forEach((itemName, list) -> {
                    setStageColor(list);   //设置关卡的颜色级别
                    itemNameAndStageEffMap.put(itemName, list.get(0).getStageEfficiency());
                });

        stageResultMapper.deleteTableTemp();   //清空数据库
        saveBatch(stageResultList);  //保存结果到数据库

        return itemNameAndStageEffMap;

    }

    //         设置关卡在前端显示的颜色,橙色(双最优):4，紫色(综合效率最优):3，蓝色(普通关卡):2，绿色(主产物期望最优):1，红色(活动):-1
    private static void setStageColor(List<StageResult> stageResultList) {
        String stageId_effMax = stageResultList.get(0).getStageId();   //拿到效率最高的关卡id
        stageResultList.get(0).setStageColor(3);  //效率最高为3

        stageResultList = stageResultList.stream()
                .filter(stageResult -> stageResult.getIsShow() == 1)  //过滤掉已经关闭的关卡
                .limit(6)  //限制个数
                .sorted(Comparator.comparing(StageResult::getApExpect))  //根据期望理智排序
                .collect(Collectors.toList());  //流转为集合

        String stageId_expectMin = stageResultList.get(0).getStageId(); //拿到期望理智最低的关卡id

        if (stageId_effMax.equals(stageId_expectMin)) {  //对比俩个id是否一致
            stageResultList.get(0).setStageColor(4); // 一致为4
        } else {
            stageResultList.get(0).setStageColor(1); // 不一致为1
        }
    }

    @Override
    public List<List<StageResult>> getResultDataByItemType() {
        QueryWrapper<StageResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", 1)
                .ge("stage_efficiency", 1.0)
                .ne("item_type", "0")
                .isNotNull("item_type")
                .orderByDesc("stage_efficiency");
        List<List<StageResult>> collectList = new ArrayList<>();
        List<StageResult> stageResultList = stageResultMapper.selectList(queryWrapper);
        stageResultList.forEach(stageResult -> stageResult.setStageEfficiency(stageResult.getStageEfficiency() / 1.25 * 100));
        stageResultList.stream().collect(Collectors.groupingBy(StageResult::getItemType))
                .forEach((k, list) -> collectList.add(list.stream().limit(7).collect(Collectors.toList())));

        if (collectList.size() < 16) throw new ServiceException(ResultCode.DATA_WRONG);

        return collectList;

//        stageResultList.forEach(efficiencyResult -> System.out.println(efficiencyResult.getStageCode() + " : " + efficiencyResult.getStageEfficiency() / 1.25));
    }

    @Override
    public Map<String, List<StageResult>> getResultDataByClosed() {
        QueryWrapper<StageResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", 0)
                .ge("stage_efficiency", 1.1)
                .isNotNull("item_type")
                .ne("item_type", "0")
                .orderByDesc("stage_id");
        List<StageResult> stageResultList = stageResultMapper.selectList(queryWrapper);
        stageResultList.forEach(stageResult -> stageResult.setStageEfficiency(stageResult.getStageEfficiency() / 1.25 * 100));
        Map<String, List<StageResult>> collect = stageResultList.stream().collect(Collectors.groupingBy(StageResult::getZoneName));
        if (collect == null) throw new ServiceException(ResultCode.DATA_NONE);

        return collect;
    }

    @Override
    public List<List<StageResult>> getResultDataByApExpect() {
        List<String> itemList = Arrays.asList("固源岩", "酮凝集", "聚酸酯", "糖", "异铁", "装置");

        List<List<StageResult>> result = new ArrayList<>();
        itemList.forEach(item ->
                result.add(stageResultMapper.selectList(
                        new QueryWrapper<StageResult>().eq("is_show", 1)  //当前正在开放的关卡
                                .eq("item_name", item)  //根据材料名称
                                .le("ap_expect", 50)
                                .orderByAsc("ap_expect")))
        );


        if (result.size() < 6) throw new ServiceException(ResultCode.DATA_WRONG);

        return result;
    }

    public List<PenguinDataResponseVo> mergePenguinData(List<PenguinDataResponseVo> stageList) {
        Map<String, List<PenguinDataResponseVo>> groupByZoneMap = new HashMap();
        List<String> zoneList = Arrays.asList("main_10", "tough_10", "main_11", "tough_11");
        zoneList.forEach(zone -> groupByZoneMap.put(zone, new ArrayList<>()));
        zoneList.forEach(zone -> stageList.forEach(stage -> {
            if (stage.getStageId().startsWith(zone)) groupByZoneMap.get(zone).add(stage); //按关卡的区域前缀分组关卡
        }));

        mergeMainAndTough(groupByZoneMap.get("main_10"), groupByZoneMap.get("tough_10"));
        mergeMainAndTough(groupByZoneMap.get("main_11"), groupByZoneMap.get("tough_11"));

        return stageList;
    }


    private static void mergeMainAndTough(List<PenguinDataResponseVo> mainList, List<PenguinDataResponseVo> toughList) {
        mainList.forEach(main -> {
            toughList.forEach(tough -> {     //将标准和磨难相同关卡下相同材料的记录进行合并
                if (main.getStageId().equals(tough.getStageId()) && main.getItemId().equals(tough.getItemId())) {
                    main.setTimes(main.getTimes() + tough.getTimes());
                    main.setQuantity(main.getQuantity() + tough.getQuantity());
                }
            });
        });
    }

}
