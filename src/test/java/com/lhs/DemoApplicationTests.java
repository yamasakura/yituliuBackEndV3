package com.lhs;

import com.alibaba.fastjson.JSONObject;
import com.lhs.common.config.FileConfig;
import com.lhs.common.util.FileUtil;
import com.lhs.model.entity.StageResult;
import com.lhs.model.entity.Item;
import com.lhs.mapper.ItemMapper;
import com.lhs.model.entity.Visits;
import com.lhs.service.StageResultService;
import com.lhs.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private StageResultService stageResultService;

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
    void zoneId() {

    }

}
