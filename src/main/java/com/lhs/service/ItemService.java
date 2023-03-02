package com.lhs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhs.model.entity.Item;

import java.util.HashMap;
import java.util.List;

public interface ItemService extends IService<Item> {

    List<Item> ItemValueCalculation(List<Item> items, HashMap<String,Double> itemNameAndStageEffMap);
    void saveByProductValue(List<Item> items);


    List<Item> findAllItemValue(Double expCoefficient);
}
