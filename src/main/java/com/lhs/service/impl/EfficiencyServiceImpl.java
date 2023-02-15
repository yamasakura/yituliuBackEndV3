package com.lhs.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.SerializationUtils;

import com.lhs.common.util.ReadFileUtil;
import com.lhs.mapper.EfficiencyResultMapper;
import com.lhs.mapper.ItemMapper;
import com.lhs.mapper.StageMapper;
import com.lhs.model.entity.EfficiencyResult;
import com.lhs.model.vo.FilePath;
import com.lhs.model.entity.Item;
import com.lhs.model.entity.Stage;
import com.lhs.model.vo.PenguinDataResponseVo;
import com.lhs.service.EfficiencyService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EfficiencyServiceImpl implements EfficiencyService {

    @Autowired
    private StageMapper stageMapper;

    @Resource
    private ItemMapper itemMapper;

    @Autowired
    private EfficiencyResultMapper efficiencyResultMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;


    @Override
    public HashMap<String, Double> initData() {



        //将企鹅物流的数据转成集合
//        String response = HttpRequestUtil.doGet("https://penguin-stats.io/PenguinStats/api/v2/_private/result/matrix/CN/global/automated");
        String response = ReadFileUtil.readFile(FilePath.Penguin + "matrix2023-02-12 20auto.json");
        String matrix = JSONObject.parseObject(response).getString("matrix");
        List<PenguinDataResponseVo> penguinDataResponseVos = JSONArray.parseArray(matrix, PenguinDataResponseVo.class);

        //将item表的各项信息转为Map
        // key为itemId ，类型String
        // value是各项信息，类型Item
        List<Item> items = itemMapper.selectList(null);
        Map<String, Item> itemValueMap = items.stream().collect(Collectors.toMap(Item::getItemId, Function.identity()));



        //将stage的各项信息转为Map
        // key为stageId ，类型String
        // value是各项信息，类型Stage
        List<Stage> stages = stageMapper.selectList(null);
        Map<String, Stage> stageInfoMap = stages.stream().collect(Collectors.toMap(Stage::getStageId, Function.identity()));
//        stageInfoMap.forEach((k, v) -> System.out.println("key:" + k + ",value:" + v));

//        过滤掉（低于300 & 不在材料表中 & 不在关卡表中）的数据
        penguinDataResponseVos = penguinDataResponseVos.stream()
                .filter(penguinData -> penguinData.getTimes() > 300 && itemValueMap.get(penguinData.getItemId()) != null
                        && stageInfoMap.get(penguinData.getStageId()) != null)
                .collect(Collectors.toList());

//        保存初次计算后的结果，本次计算仅计算当前
        List<EfficiencyResult> efficiencyResultList = new ArrayList<>();
        Long id = 0L;
        for (PenguinDataResponseVo penguinData : penguinDataResponseVos) {
            EfficiencyResult efficiencyResult = new EfficiencyResult();
            efficiencyResult.setId(id++);
            Stage stage = stageInfoMap.get(penguinData.getStageId());
            Item item = itemValueMap.get(penguinData.getItemId());
            Double knockRating = ((double) penguinData.getQuantity() / (double) penguinData.getTimes());
//            System.out.println(penguinData.getStageId()+","+ penguinData.getQuantity()+" / "+ penguinData.getTimes()+","+knockRating );
            efficiencyResult.setStageId(stage.getStageId());
            efficiencyResult.setStageCode(stage.getStageCode());
            efficiencyResult.setSampleSize(penguinData.getTimes());
            efficiencyResult.setItemId(item.getItemId());
            efficiencyResult.setItemName(item.getItemName());
            efficiencyResult.setKnockRating(knockRating);

            if (knockRating > 0) efficiencyResult.setApExpect(stage.getApCost() / knockRating);

            efficiencyResult.setApCost(stage.getApCost());
            efficiencyResult.setResult(item.getItemValue() * knockRating);
            if (item.getItemName().equals(stage.getMain())) {
                efficiencyResult.setMain(stage.getMain());
                efficiencyResult.setItemType(stage.getItemType());
                efficiencyResult.setMainLevel(stage.getMainLevel());
            }


            efficiencyResult.setSecondary(stage.getSecondary());
            efficiencyResult.setIsShow(stage.getIsShow());
            efficiencyResult.setIsValue(stage.getIsValue());
            efficiencyResult.setStageColor(2);
            efficiencyResultList.add(efficiencyResult);
            if (stage.getIsShow() == 1 && stage.getIsValue() == 0) {
                EfficiencyResult efficiencyResultCopy = SerializationUtils.clone(efficiencyResult);
                efficiencyResultCopy.setId(id + 100000);
                efficiencyResultCopy.setStageId(stage.getStageId() + "_LMD");
                efficiencyResultCopy.setResult(efficiencyResultCopy.getResult() + stage.getApCost() * 0.09);
                efficiencyResultCopy.setStageColor(-1);
                efficiencyResultList.add(efficiencyResultCopy);
            }

        }

//        把计算结果根据stageId分组
        Map<String, List<EfficiencyResult>> effGroupByStageId = efficiencyResultList.stream()
                .collect(Collectors.groupingBy(EfficiencyResult::getStageId));
        Set<String> stageIdSet = effGroupByStageId.keySet();

        //将关卡的单项结果相加算出最终结果
        for (String stageId : stageIdSet) {
            List<EfficiencyResult> efficiencyResultListByStageId = effGroupByStageId.get(stageId);
            Double apCost = efficiencyResultListByStageId.get(0).getApCost();
            double efficiency = efficiencyResultListByStageId.stream().mapToDouble(EfficiencyResult::getResult).sum();
            efficiencyResultListByStageId.forEach(efficiencyResult -> efficiencyResult
                    .setStageEfficiency((efficiency + apCost * 0.054) / apCost));
        }



        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        EfficiencyResultMapper mapper = session.getMapper(EfficiencyResultMapper.class);
        mapper.deleteTableTemp();

        for (int i = 0; i < efficiencyResultList.size(); i++) {
            mapper.insert(efficiencyResultList.get(i));
            if (i % 5000 == 4999) {
                session.commit();
                session.clearCache();
            }
        }

        session.commit();
        session.clearCache();
        session.close();



        Map<String, List<EfficiencyResult>> collect = efficiencyResultList.stream()
                .filter(efficiencyResult ->efficiencyResult.getMainLevel()!=null&&efficiencyResult.getMainLevel()>1&&efficiencyResult.getIsValue()==1)
                .sorted(Comparator.comparing(EfficiencyResult::getStageEfficiency).reversed())
                .collect(Collectors.groupingBy(EfficiencyResult::getItemType));

        String[] item_t3List = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇","酮凝集组", "RMA70-12", "炽合金",
                "研磨石", "糖组","聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液","转质盐组"};


        HashMap<String, Double> itemNameAndStageEffMap = new HashMap<>();
        Arrays.stream(item_t3List).sequential().forEach(itemName->itemNameAndStageEffMap.put(itemName,1.25 /collect.get(itemName).get(0).getStageEfficiency()));
        Arrays.stream(item_t3List).sequential().forEach(itemName-> System.out.println(itemName+" : "+collect.get(itemName).get(0).getStageCode()+" : "+collect.get(itemName).get(0).getStageEfficiency()));


        return itemNameAndStageEffMap;

    }

    @Override
    public List<List<EfficiencyResult>> getResultDataByItemType() {
        QueryWrapper<EfficiencyResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_show", 1);
        queryWrapper.ge("stage_efficiency", 0.8);
        queryWrapper.ge("main_level", 1);
        queryWrapper.orderByDesc("stage_efficiency");
        List<EfficiencyResult> efficiencyResultList = efficiencyResultMapper.selectList(queryWrapper);
        efficiencyResultList.forEach(efficiencyResult -> efficiencyResult.setStageEfficiency(efficiencyResult.getStageEfficiency()/1.25*100));

        Map<String, List<EfficiencyResult>> collect = efficiencyResultList.stream().collect(Collectors.groupingBy(EfficiencyResult::getItemType));
        collect.remove("1");
        Set<String> itemTypes = collect.keySet();
        List<List<EfficiencyResult>> collectList = new ArrayList<>();
                itemTypes.forEach(itemType-> collectList.add(collect.get(itemType)));

        return collectList;

//        efficiencyResultList.forEach(efficiencyResult -> System.out.println(efficiencyResult.getStageCode() + " : " + efficiencyResult.getStageEfficiency() / 1.25));

    }


}
