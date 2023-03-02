package com.lhs.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhs.common.util.FileUtil;
import com.lhs.mapper.ItemMapper;
import com.lhs.common.config.FileConfig;
import com.lhs.model.entity.Item;
import com.lhs.model.vo.ItemCost;
import com.lhs.service.ItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper,Item> implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    /**
     *  //根据上面得到的蓝材料对应的常驻最高关卡效率En计算蓝材料价值Vn+1  ，  Vn+1= Vn*1.25/En
     * @param items   材料信息
     * @param itemNameAndStageEff  map<蓝材料名称，蓝材料对应的常驻最高关卡效率En>
     * @return  新的材料价值集合
     */
    @Override
    @Transactional
    public List<Item> ItemValueCalculation(List<Item> items, HashMap<String, Double> itemNameAndStageEff) {
        //读取根据Vn计算出的副产物价值
        JSONObject byProductJson = JSONObject.parseObject(FileUtil.read(FileConfig.Item + "byProduct.json"));
        //读取加工站合成表
        JSONObject composite_tableJson = JSONObject.parseObject(FileUtil.read(FileConfig.Item + "composite_table.json"));

        Map<String, Item> itemValueMap = items.stream().collect(Collectors.toMap(Item::getItemName, Function.identity()));  //将旧的材料Vn集合转成map方便调用


        //循环itemNameAndStageEff   (id,En)为（蓝材料名称，蓝材料对应的常驻最高关卡效率En）
        itemNameAndStageEff.forEach((id,En)->{
             //在itemValueMap 设置新的材料价值Vn+1 ， Vn+1= Vn*1.25/En
            itemValueMap.get(id).setItemValue(itemValueMap.get(id).getItemValue() * 1.25 / En);
        });


        //这里是为了按顺序计算除了蓝材料之外的材料价值
        String[] itemList = new String[]{"固源岩", "糖", "聚酸酯", "异铁", "酮凝集", "装置","源岩", "代糖", "酯原料", "异铁碎片",
                "双酮", "破损装置","提纯源岩", "糖聚块", "聚酸酯块","异铁块", "酮阵列", "改量装置","白马醇", "三水锰矿", "五水研磨石",
                "RMA70-24", "聚合凝胶","炽合金块", "晶体电路", "切削原液", "精炼溶剂","晶体电子单元","聚合剂","双极纳米片","D32钢"
                ,"烧结核凝晶"
        };


        //按顺序读取材料合成表
        try {
            Arrays.stream(itemList)
                    .forEach(itemName->{
                Integer rarity = itemValueMap.get(itemName).getRarity();//获取这个材料的品质
                List<ItemCost> itemCosts = JSONArray.parseArray(composite_tableJson.getString(itemName), ItemCost.class); //将材料消耗转为集合
                double itemValueNew = 0.0;

                if(rarity<3){
                    for(ItemCost itemCost :itemCosts){      //灰，绿色品质是向下拆解   Vn+1 += Vn+1/合成需求个数
                        itemValueNew += itemValueMap.get(itemCost.getId()).getItemValue()/itemCost.getCount();
                    }
                    //灰，绿色品质需要加副产物价值
                    itemValueNew += Double.parseDouble(byProductJson.getString("rarity_"+(rarity))) -0.45*rarity;
                }else  {
                    for(ItemCost itemCost :itemCosts){
                        //紫，金色品质是向上合成    Vn+1 +=Vn+1*合成需求个数
                        itemValueNew += itemValueMap.get(itemCost.getId()).getItemValue()*itemCost.getCount();
                    }
                    //紫，金色品质是减副产物价值
                    itemValueNew -= Double.parseDouble(byProductJson.getString("rarity_"+(rarity-1))) -0.45*rarity;
                }
                itemValueMap.get(itemName).setItemValue(itemValueNew);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        updateBatchById(items);

        saveByProductValue(items);  //保存Vn+1的加工站副产物平均产出价值


      return items;
    }

    @Override
    public void saveByProductValue(List<Item> items) {
        double knockRating = 0.18;
        HashMap<Object, Object> hashMap = new HashMap<>();
        items.stream()
                .filter(item -> item.getWeight() > 0)
                .collect(Collectors.groupingBy(Item::getRarity))
                .forEach((rarity,list)->{
                    hashMap.put( "rarity_"+rarity, list.stream().mapToDouble(item -> item.getItemValue() * item.getWeight())
                        .sum() / items.size() * knockRating );
                } );
        FileUtil.save(FileConfig.Item, "byProduct.json", JSON.toJSONString(hashMap));
    }




    @Override
    public List<Item> findAllItemValue(Double expCoefficient) {

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("exp_coefficient",0.625);
        return itemMapper.selectByMap(hashMap);
    }

}
