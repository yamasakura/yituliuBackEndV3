package com.lhs.controller.api;


import com.alibaba.fastjson.JSONArray;
import com.lhs.common.util.Result;
import com.lhs.model.entity.EfficiencyResult;
import com.lhs.model.entity.Item;
import com.lhs.service.EfficiencyService;
import com.lhs.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "获取数据API")
@RequestMapping(value = "/api")
@CrossOrigin()
public class ApiController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private EfficiencyService efficiencyService;

    @ApiOperation("获取物品价值")
    @GetMapping("/find/item/value")
    public Result findAllItem(@RequestParam Double expCoefficient) {
        List<Item> all = itemService.findAllItemValue(expCoefficient);
        return Result.success(all);
    }


    @ApiOperation("获取蓝材料最优图")
    @GetMapping("/find/stage/t3")
    public Result findByTypeAndEndsOrderByEfficiencyDesc(@RequestParam String expCoefficient) {

        List<List<EfficiencyResult>> resultDataByItemType = efficiencyService.getResultDataByItemType();
        return Result.success(resultDataByItemType);
    }

    @ApiOperation("更新一图流")
    @GetMapping("/update")
    public Result updateData() {
        double start = System.currentTimeMillis();

        List<Item> items = itemService.findAllItemValue(0.625);
        HashMap<String, Double> itemNameAndStageEffMap = efficiencyService.initData();
        items = itemService.ItemValueCalculation(items, itemNameAndStageEffMap);
        itemService.saveByProductValue(items);
        efficiencyService.initData();

        double end = System.currentTimeMillis();
        String text = "用时:---" + (end - start) + "ms";
        return Result.success(text);
    }
}
