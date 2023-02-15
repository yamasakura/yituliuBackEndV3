package com.lhs.service;

import com.lhs.model.entity.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ItemService {

    List<Item> ItemValueCalculation(List<Item> items, HashMap<String,Double> itemNameAndStageEffMap);
    void saveByProductValue(List<Item> items);


    List<Item> findAllItemValue(Double expCoefficient);
}
