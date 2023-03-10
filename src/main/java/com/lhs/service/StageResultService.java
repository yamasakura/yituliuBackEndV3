package com.lhs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lhs.model.entity.StageResult;
import com.lhs.model.entity.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface StageResultService extends IService<StageResult> {

    HashMap<String, Double> initData(List<Item> items);

    List<List<StageResult>> getResultDataByItemType();


    Map<String,List<StageResult>> getResultDataByClosed();

    List<List<StageResult>> getResultDataByApExpect();
}
