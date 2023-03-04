package com.lhs.controller.api;


import com.lhs.common.util.Result;
import com.lhs.model.entity.StageResult;
import com.lhs.model.entity.Item;
import com.lhs.service.ItemService;
import com.lhs.service.StageService;
import com.lhs.service.StageResultService;

import com.lhs.service.StorePermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private StorePermService storePermService;

    @Autowired
    private StageService stageService;

    @ApiOperation("获取物品价值")
    @GetMapping("/find/item/value")
    public Result findAllItem(@RequestParam Double expCoefficient) {
        List<Item> all = itemService.findAllItemValue(expCoefficient);
        return Result.success(all);
    }


    @ApiOperation("获取蓝材料推荐关卡按效率倒序")
    @GetMapping("/find/stage/t3")
    public Result getStageResultDataByT3(@RequestParam String expCoefficient) {
        List<List<StageResult>> resultDataByItemType = stageResultService.getResultDataByItemType();
        return Result.success(resultDataByItemType);
    }

    @ApiOperation("获取绿材料推荐关卡按期望正序")
    @GetMapping("/find/stage/t2")
    public Result getStageResultDataByT2(@RequestParam String expCoefficient) {
        List<List<StageResult>> resultDataByItemType = stageResultService.getResultDataByApExpect();
        return Result.success(resultDataByItemType);
    }

    @ApiOperation("获取已关闭活动地图")
    @GetMapping("/find/stage/closed")
    public Result getStageResultDataByClosed(@RequestParam String expCoefficient) {
       Map<String, List<StageResult>> resultDataByClosed = stageResultService.getResultDataByClosed();
        return Result.success(resultDataByClosed);
    }

    @ApiOperation("更新关卡推荐")
    @GetMapping("/update/stage")
    public Result updateStageData() {
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

    @ApiOperation("更新常驻商店")
    @GetMapping("/update/store/perm")
    public Result updateStorePermData() {
        double start = System.currentTimeMillis();
         storePermService.updateStorePermDate();
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
