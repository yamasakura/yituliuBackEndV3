package com.lhs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lhs.common.util.ReadFileUtil;
import com.lhs.common.util.SaveFile;
import com.lhs.mapper.EfficiencyResultMapper;
import com.lhs.mapper.ItemMapper;
import com.lhs.model.vo.FilePath;
import com.lhs.model.entity.Item;
import com.lhs.model.vo.ItemCost;
import com.lhs.service.ItemService;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public List<Item> ItemValueCalculation(List<Item> items, HashMap<String, Double> itemNameAndStageEff) {

        String byProductStr = ReadFileUtil.readFile(FilePath.Item + "byProduct.json");
        JSONObject byProductJson = JSONObject.parseObject(byProductStr);

        String composite_tableStr = ReadFileUtil.readFile(FilePath.Item + "composite_table.json");
        JSONObject composite_tableJson = JSONObject.parseObject(composite_tableStr);


        String[] item_t1List = new String[]{"源岩", "代糖", "酯原料", "异铁碎片", "双酮", "破损装置",};
        String[] item_t2List = new String[]{"固源岩", "糖", "聚酸酯", "异铁", "酮凝集", "装置",};
        String[] item_t3List = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12",
                "炽合金", "研磨石", "糖组", "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液", "转质盐组"};
        String[] item_t4List = new String[]{"提纯源岩", "糖聚块", "聚酸酯块", "异铁块", "酮阵列", "改量装置",
                "白马醇", "三水锰矿", "五水研磨石", "RMA70-24", "聚合凝胶", "炽合金块", "晶体电路", "切削原液", "精炼溶剂",
        };
        String[] item_t5List = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12",
                "炽合金", "研磨石", "糖组", "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液", "转质盐组"};


        Map<String, Item> itemValueMap = items.stream().collect(Collectors.toMap(Item::getItemName, Function.identity()));


        for (String itemName : item_t3List) {
            Item item = itemValueMap.get(itemName);
            System.out.println(itemName+" : "+ itemNameAndStageEff.get(itemName));
            item.setItemValue(item.getItemValue() * itemNameAndStageEff.get(itemName));
//            item.setItemValue(item.getItemValue() * 1.0);
            itemValueMap.put(itemName, item);
        }

        System.out.println("————————————————————————————————————————————————————————————————————————————————————————");

        for (String itemName : composite_tableJson.keySet()) {

//            System.out.println(itemName);

            List<ItemCost> itemCosts = JSONArray.parseArray(composite_tableJson.getString(itemName), ItemCost.class);
            double itemValueNew = 0.0;
            if(itemCosts.size()==1){
                for(ItemCost itemCost :itemCosts){
                    itemValueNew += itemValueMap.get(itemCost.getId()).getItemValue()/Double.parseDouble(itemCost.getCount().toString());
                }
            }else {
                for(ItemCost itemCost :itemCosts){
                    itemValueNew += itemValueMap.get(itemCost.getId()).getItemValue()*Double.parseDouble(itemCost.getCount().toString());
                }
            }

            String type = itemValueMap.get(itemName).getType();
            if("grey".equals(type)) itemValueNew += Double.parseDouble(byProductJson.getString("t1"));
            if("green".equals(type)) itemValueNew += Double.parseDouble(byProductJson.getString("t2"));
//            if("blue".equals(type)) itemValueNew += Double.parseDouble(byProductJson.getString("t3"));
            if("purple".equals(type)) itemValueNew -= Double.parseDouble(byProductJson.getString("t3"));
            if("orange".equals(type)) itemValueNew -= Double.parseDouble(byProductJson.getString("t4"));

            Item item = itemValueMap.get(itemName);
            item.setItemValue(itemValueNew);
        }

        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH);
        ItemMapper mapper = session.getMapper(ItemMapper.class);
        mapper.deleteTableTemp();

        for (int i = 0; i < items.size(); i++) {
            mapper.insert(items.get(i));
            if (i % 5000 == 4999) {
                session.commit();
                session.clearCache();
            }
        }

        session.commit();
        session.clearCache();
        session.close();

//        items.stream().forEach(item -> System.out.println(item));

      return items;
    }

    @Override
    public void saveByProductValue(List<Item> items) {
        String workShopKnockRatingStr = ReadFileUtil.readFile(FilePath.Item + "workShopKnockRating.json");
        JSONObject workShopKnockRatingJson = JSONObject.parseObject(workShopKnockRatingStr);

        String[] item_t1List = new String[]{"源岩", "代糖", "酯原料", "异铁碎片", "双酮", "破损装置",};
        String[] item_t2List = new String[]{"固源岩", "糖", "聚酸酯", "异铁", "酮凝集", "装置",};
        String[] item_t3List = new String[]{"全新装置", "异铁组", "轻锰矿", "凝胶", "扭转醇", "酮凝集组", "RMA70-12",
                "炽合金", "研磨石", "糖组", "聚酸酯组", "晶体元件", "固源岩组", "半自然溶剂", "化合切削液", "转质盐组"};
        String[] item_t4List = new String[]{"提纯源岩", "糖聚块", "聚酸酯块", "异铁块", "酮阵列", "改量装置",
                "白马醇", "三水锰矿", "五水研磨石", "RMA70-24", "聚合凝胶", "炽合金块", "晶体电路", "切削原液", "精炼溶剂",
        };

        double knockRating = 0.18;

        double byProduct_t1 = items.stream()
                .filter(item -> item.getWorkShopKnockRating() > 0 && "grey".equals(item.getType()))
                .mapToDouble(item -> item.getItemValue() * item.getWorkShopKnockRating())
                .sum() / items.size() * knockRating - 0.45;

        double byProduct_t2 = items.stream()
                .filter(item -> item.getWorkShopKnockRating() > 0 && "green".equals(item.getType()))
                .mapToDouble(item -> item.getItemValue() * item.getWorkShopKnockRating())
                .sum() / items.size() * knockRating - 0.9;

        double byProduct_t3 = items.stream()
                .filter(item -> item.getWorkShopKnockRating() > 0 && "blue".equals(item.getType()))
                .mapToDouble(item -> item.getItemValue() * item.getWorkShopKnockRating())
                .sum() / items.size() * knockRating - 1.35;

        double byProduct_t4 = items.stream()
                .filter(item -> item.getWorkShopKnockRating() > 0 && "purple".equals(item.getType()))
                .mapToDouble(item -> item.getItemValue() * item.getWorkShopKnockRating())
                .sum() / items.size() * knockRating - 1.8;

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("t1", byProduct_t1);
        hashMap.put("t2", byProduct_t2);
        hashMap.put("t3", byProduct_t3);
        hashMap.put("t4", byProduct_t4);
        SaveFile.save(FilePath.Item, "byProduct.json", JSON.toJSONString(hashMap));

    }

    @Override
    public List<Item> findAllItemValue(Double expCoefficient) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("exp_coefficient",0.625);
        return itemMapper.selectByMap(hashMap);
    }

}
