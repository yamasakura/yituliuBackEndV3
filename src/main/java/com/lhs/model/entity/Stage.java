package com.lhs.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("stage")
public class Stage {

    @TableId
    private String stageId;

    private String stageCode;

    private String zoneId;

    private Integer code;

    private Double apCost;

    private String main;

    private Integer mainLevel;

    private String secondary;

    private String secondaryId;

    private Double spm;

    private String itemType;

    private Integer stageState;

    private Integer isValue;

    private Integer isShow;

    private String stageType;

    private String activityName;

    private Integer minClearTime;

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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public Integer getMainLevel() {
        return mainLevel;
    }

    public void setMainLevel(Integer mainLevel) {
        this.mainLevel = mainLevel;
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

    public String getStageType() {
        return stageType;
    }

    public void setStageType(String stageType) {
        this.stageType = stageType;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Integer getMinClearTime() {
        return minClearTime;
    }

    public void setMinClearTime(Integer minClearTime) {
        this.minClearTime = minClearTime;
    }
}
