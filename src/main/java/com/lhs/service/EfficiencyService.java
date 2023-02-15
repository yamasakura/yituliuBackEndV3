package com.lhs.service;

import com.lhs.model.entity.EfficiencyResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


public interface EfficiencyService {

    HashMap<String, Double> initData();

    List<List<EfficiencyResult>> getResultDataByItemType();
}
