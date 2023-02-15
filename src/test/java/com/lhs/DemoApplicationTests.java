package com.lhs;

import com.alibaba.fastjson.JSONObject;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.model.vo.FilePath;
import com.lhs.model.entity.Item;
import com.lhs.mapper.ItemMapper;
import com.lhs.service.EfficiencyService;
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
    private EfficiencyService efficiencyService;

    @Autowired
    private ItemService itemService;

    @Test
    void intiData() {
        List<Item> items = itemMapper.selectList(null);
        HashMap<String, Double> itemNameAndStageEffMap = efficiencyService.initData();
        items = itemService.ItemValueCalculation(items, itemNameAndStageEffMap);
        itemService.saveByProductValue(items);
        efficiencyService.initData();

    }

    @Test
    void selectData() {
        efficiencyService.getResultDataByItemType();
    }

    @Test
    void updateItem() {
        List<Item> items = itemMapper.selectList(null);
        String workShopKnockRatingStr = ReadFileUtil.readFile(FilePath.Item + "workShopKnockRating.json");
        JSONObject workShopKnockRatingJson = JSONObject.parseObject(workShopKnockRatingStr);
        for(Item item:items){
          if(workShopKnockRatingJson.getString(item.getItemName())!=null){
              item.setWorkShopKnockRating(Double.parseDouble(workShopKnockRatingJson.getString(item.getItemName())));

          }else {
              item.setWorkShopKnockRating(null);
          }
            int insert = itemMapper.updateById(item);
        }
    }

    @Test
    void ByProductValue() {
//     itemService.saveByProductValue(itemMapper.selectList(null));
        itemService.ItemValueCalculation(itemMapper.selectList(null),new HashMap<>());
    }




}
