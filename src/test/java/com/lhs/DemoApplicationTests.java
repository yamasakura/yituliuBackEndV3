package com.lhs;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.common.config.FileConfig;
import com.lhs.common.util.FileUtil;
import com.lhs.mapper.StorePermMapper;
import com.lhs.model.entity.StageResult;
import com.lhs.model.entity.Item;
import com.lhs.mapper.ItemMapper;
import com.lhs.model.entity.StorePerm;
import com.lhs.model.entity.Visits;
import com.lhs.service.StageResultService;
import com.lhs.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private StageResultService stageResultService;
    @Autowired
    private StorePermMapper storePermMapper;
    @Autowired
    private ItemService itemService;

    @Test
    void intiData() {
        List<Item> items = itemMapper.selectList(null);
        HashMap<String, Double> itemNameAndStageEffMap = stageResultService.initData(items);
        items = itemService.ItemValueCalculation(items, itemNameAndStageEffMap);
        itemService.saveByProductValue(items);
        stageResultService.initData(items);

    }

    @Test
    void selectData() {
        List<List<StageResult>> resultDataByItemType = stageResultService.getResultDataByItemType();
        System.out.println(resultDataByItemType);
    }

    @Test
    void updateItem() {
        List<Item> items = itemMapper.selectList(null);
        String workShopKnockRatingStr = FileUtil.read(FileConfig.Item + "workShopKnockRating.json");
        JSONObject workShopKnockRatingJson = JSONObject.parseObject(workShopKnockRatingStr);
        for (Item item : items) {
            if (workShopKnockRatingJson.getString(item.getItemName()) != null) {
                item.setWeight(Double.parseDouble(workShopKnockRatingJson.getString(item.getItemName())));
            } else {
                item.setWeight(null);
            }
            int insert = itemMapper.updateById(item);
        }
    }


    @Test
    void save() {
        List<Item> items = itemMapper.selectList(null);
        itemService.saveByProductValue(items);
    }

    @Test
    void visitsAdd() {
        Visits visits = new Visits();
        visits.setVisits(3);
        visits.updateVisits();
        visits.updateVisits();
        visits.updateVisits();
        System.out.println(visits);
    }

    @Test
    void readStorePermJson() {

        String read = FileUtil.read(FileConfig.Item + "permStoreData.json");
        List<StorePerm> storePerms = JSONArray.parseArray(read, StorePerm.class);
                   int id=0;
                   String type = "orange";
                   for(StorePerm storePerm:storePerms){
                       if(!type.equals(storePerm.getStoreType())){
                           type = storePerm.getStoreType();
                           if(type.equals("green")) id=99;
                           if(type.equals("purple")) id=199;
                           if(type.equals("yellow")) id=299;
                           if(type.equals("grey")) id=399;
                       }
                       id++;
                       storePerm.setId(id);
                       storePermMapper.insert(storePerm);
                   }
    }
    @Test
    void updateStore(){
        List<StorePerm> storePerms = storePermMapper.selectList(null);
//        Map<String, Item> collect = itemService.findAllItemValue(0.625).stream().collect(Collectors.toMap(Item::getItemName, Function.identity()));
//        storePerms.forEach(storePerm -> {
//            storePerm.setCostPer(collect.get(storePerm.getItemName()).getItemValue()/storePerm.getCost()/storePerm.getQuantity());
//            storePerm.setRarity(collect.get(storePerm.getItemName()).getRarity());
//            System.out.println(storePerm);
//        });
    }

}
