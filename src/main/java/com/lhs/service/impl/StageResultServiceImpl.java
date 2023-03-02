package com.lhs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhs.mapper.StageResultMapper;
import com.lhs.common.util.FileUtil;
import com.lhs.mapper.StageMapper;
import com.lhs.model.entity.StageResult;
import com.lhs.common.config.FileConfig;
import com.lhs.model.entity.Item;
import com.lhs.model.entity.Stage;
import com.lhs.model.vo.PenguinDataResponseVo;
import com.lhs.service.StageResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service

public class StageResultServiceImpl extends ServiceImpl<StageResultMapper, StageResult> implements StageResultService {

    @Autowired
    private StageMapper stageMapper;


    @Autowired
    private StageResultMapper stageResultMapper;


    @Override
    @Transactional
    public HashMap<String, Double> initData(List<Item> items) {

        //将企鹅物流的数据转成集合
//        String response = HttpRequestUtil.doGet("https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated");
        String response = FileUtil.read(FileConfig.Penguin + "matrix2023-02-12 20auto.json");
        String matrix = JSONObject.parseObject(response).getString("matrix");
        List<PenguinDataResponseVo> penguinDataResponseVos = JSONArray.parseArray(matrix, PenguinDataResponseVo.class);

        Map<String, Item> itemValueMap = items.stream().collect(Collectors.toMap(Item::getItemId, Function.identity())); //将item表的各项信息转为Map  <itemId,Item类 >
        Map<String, Stage> stageInfoMap = stageMapper.selectList(null).stream().collect(Collectors.toMap(Stage::getStageId, Function.identity()));  //将stage的各项信息转为Map <stageId,stage类 >


//      过滤掉（低于300 & 不在材料表中 & 不在关卡表中）的数据
        penguinDataResponseVos = penguinDataResponseVos.stream()
                .filter(penguinData -> penguinData.getTimes() > 300 && itemValueMap.get(penguinData.getItemId()) != null
                        && stageInfoMap.get(penguinData.getStageId()) != null)
                .collect(Collectors.toList());


//      保存企鹅物流每一条记录的结果
        List<StageResult> stageResultList = new ArrayList<>(); //关卡效率计算结果
        long id = new Date().getTime() * 100000;   //id为时间戳后加00001至99999
        for (PenguinDataResponseVo penguinData : penguinDataResponseVos) {
            StageResult efficiencyResult = new StageResult();
            Stage stage = stageInfoMap.get(penguinData.getStageId());
            Item item = itemValueMap.get(penguinData.getItemId());

            efficiencyResult.setId(id++);
            efficiencyResult.setStageId(stage.getStageId());
            efficiencyResult.setStageCode(stage.getStageCode());
            efficiencyResult.setApCost(stage.getApCost());
            if (item.getItemName().equals(stage.getMain()) && !"0".equals(stage.getMain())) {   // 只有该关卡的主产物的计算结果才保存材料类型和材料等级
                efficiencyResult.setMain(stage.getMain());
                efficiencyResult.setItemType(stage.getItemType());
            }

            efficiencyResult.setSecondary(stage.getSecondary());
            efficiencyResult.setIsShow(stage.getIsShow());
            efficiencyResult.setIsValue(stage.getIsValue());

            Double knockRating = ((double) penguinData.getQuantity() / (double) penguinData.getTimes());  //材料掉率

            efficiencyResult.setSampleSize(penguinData.getTimes());
            efficiencyResult.setItemId(item.getItemId());
            efficiencyResult.setItemName(item.getItemName());
            efficiencyResult.setKnockRating(knockRating);
            if (knockRating > 0) efficiencyResult.setApExpect(stage.getApCost() / knockRating);
            efficiencyResult.setResult(item.getItemValue() * knockRating);

            efficiencyResult.setStageColor(2);
            stageResultList.add(efficiencyResult);

            //前端会展示但是不用来定价的为活动本
            if (stage.getIsShow() == 1 && stage.getIsValue() == 0) {
                StageResult efficiencyResultCopy = SerializationUtils.clone(efficiencyResult);
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
                    setStageColor(list);
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
        queryWrapper.eq("is_show", 1);
        queryWrapper.ge("stage_efficiency", 1.0);
        queryWrapper.isNotNull("item_type");
        queryWrapper.ne("item_type", "0");
        queryWrapper.orderByDesc("stage_efficiency");
        List<StageResult> stageResultList = stageResultMapper.selectList(queryWrapper);
        stageResultList.forEach(stageResult -> stageResult.setStageEfficiency(stageResult.getStageEfficiency() / 1.25 * 100));
        Map<String, List<StageResult>> collect = stageResultList.stream().collect(Collectors.groupingBy(StageResult::getItemType));
        Set<String> itemTypes = collect.keySet();
        List<List<StageResult>> collectList = new ArrayList<>();
        itemTypes.forEach(itemType -> collectList.add(collect.get(itemType)));

        return collectList;

//        stageResultList.forEach(efficiencyResult -> System.out.println(efficiencyResult.getStageCode() + " : " + efficiencyResult.getStageEfficiency() / 1.25));
    }


}
