package com.lhs.controller.api;


import com.lhs.common.util.Result;
import com.lhs.model.entity.StageResult;
import com.lhs.model.entity.Item;
import com.lhs.service.ItemService;
import com.lhs.service.StageService;
import com.lhs.service.StageResultService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private StageResultService stageResultService;

    @Autowired
    private StageService stageService;

    @ApiOperation("获取物品价值")
    @GetMapping("/find/item/value")
    public Result findAllItem(@RequestParam Double expCoefficient) {
        List<Item> all = itemService.findAllItemValue(expCoefficient);
        return Result.success(all);
    }


    @ApiOperation("获取蓝材料最优图")
    @GetMapping("/find/stage/t3")
    public Result findByTypeAndEndsOrderByEfficiencyDesc(@RequestParam String expCoefficient) {
        List<List<StageResult>> resultDataByItemType = stageResultService.getResultDataByItemType();
        return Result.success(resultDataByItemType);
    }

    @ApiOperation("更新一图流")
    @GetMapping("/update")
    public Result updateData() {
        double start = System.currentTimeMillis();
        List<Item> items = itemService.findAllItemValue(0.625);   //找出该经验书价值系数版本的材料价值Vn
        HashMap<String, Double> itemNameAndStageEffMap = stageResultService.initData(items);//计算第一次关卡效率 拿到map<蓝材料名称，蓝材料对应的常驻最高关卡效率En>
        System.out.println(itemNameAndStageEffMap);
        items = itemService.ItemValueCalculation(items,itemNameAndStageEffMap);  //用上面的map计算新的材料价值
        stageResultService.initData(items);      //用Vn+1再次计算关卡效率En+1
        double end = System.currentTimeMillis();
        String text = "用时:---" + (end - start) + "ms";
        return Result.success(text);
    }

    @ApiOperation(value = "关卡信息导入")
    @PostMapping("/import/StageData")
    public Result importStageData(MultipartFile file) {
//        System.out.println("导入了");
        stageService.importStageData(file);
        return Result.success("成功");
    }
}
