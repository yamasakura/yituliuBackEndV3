package com.lhs.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("stage")
public class Stage {

    @TableId
    @ExcelProperty("关卡Id")
    private String stageId;    //关卡ID
    @ExcelProperty("关卡名称")
    private String stageCode; //关卡名称
    @ExcelProperty("区域Id")
    private String zoneId;  //区域Id
    @ExcelProperty("消耗理智")
    private Double apCost; //理智消耗
    @ExcelProperty("主产物")
    private String main;  //主产物
    @ExcelProperty("副产物")
    private String secondary;   //副产物
    @ExcelProperty("理论通关时间")
    private Integer minClearTime;  //最短用时

    private Integer mainRarity;  //主产等级

    private String secondaryId; //副产物ID

    private Double spm;  //每分钟消耗理智

    private String itemType;  //物品系列，比如固源岩属于固源岩组系列

    private Integer stageState;  //关卡状态

    private Integer isValue;  //是否用于定价

    private Integer isShow;  //是否在前端显示

    private String type;  //关卡类型

    private String zoneName;  //章节名称



}
