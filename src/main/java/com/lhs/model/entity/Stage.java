package com.lhs.model.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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


    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Double getApCost() {
        return apCost;
    }

    public void setApCost(Double apCost) {
        this.apCost = apCost;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public Integer getMainRarity() {
        return mainRarity;
    }

    public void setMainRarity(Integer mainRarity) {
        this.mainRarity = mainRarity;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
    }

    public Double getSpm() {
        return spm;
    }

    public void setSpm(Double spm) {
        this.spm = spm;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getStageState() {
        return stageState;
    }

    public void setStageState(Integer stageState) {
        this.stageState = stageState;
    }

    public Integer getIsValue() {
        return isValue;
    }

    public void setIsValue(Integer isValue) {
        this.isValue = isValue;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Integer getMinClearTime() {
        return minClearTime;
    }

    public void setMinClearTime(Integer minClearTime) {
        this.minClearTime = minClearTime;
    }

    @Override
    public String toString() {
        return "Stage{" +
                "stageId='" + stageId + '\'' +
                ", stageCode='" + stageCode + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", apCost=" + apCost +
                ", main='" + main + '\'' +
                ", mainRarity=" + mainRarity +
                ", secondary='" + secondary + '\'' +
                ", secondaryId='" + secondaryId + '\'' +
                ", spm=" + spm +
                ", itemType='" + itemType + '\'' +
                ", stageState=" + stageState +
                ", isValue=" + isValue +
                ", isShow=" + isShow +
                ", type='" + type + '\'' +
                ", zoneName='" + zoneName + '\'' +
                ", minClearTime=" + minClearTime +
                '}';
    }
}
