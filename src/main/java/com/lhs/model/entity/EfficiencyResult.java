package com.lhs.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("efficiency_result")
public class EfficiencyResult implements Serializable {

    @TableId
    private Long id;
    private String stageId;  // 关卡id
    private Integer sampleSize;  // 样本次数
    private String itemId;   //产物ID
    private String itemName;    //产物名称
    private String stageCode;   // 关卡名称
    private Double knockRating;   // 概率
    private Double result;   // 单项结果
    private Double apExpect; // 期望理智
    private String main; // 主产物
    private Integer mainLevel; // 主产物
    private String itemType;  //材料类型
    private String secondary; // 副产物
    private Double apCost;  //理智消耗
    private Integer isShow;   // 是否显示
    private Integer isValue;  //是否参与定价
    private Double stageEfficiency;    //理智转化率

    private Integer stageColor;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public Integer getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Integer sampleSize) {
        this.sampleSize = sampleSize;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public Double getKnockRating() {
        return knockRating;
    }

    public void setKnockRating(Double knockRating) {
        this.knockRating = knockRating;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Double getApExpect() {
        return apExpect;
    }

    public void setApExpect(Double apExpect) {
        this.apExpect = apExpect;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getSecondary() {
        return secondary;
    }

    public void setSecondary(String secondary) {
        this.secondary = secondary;
    }

    public Double getApCost() {
        return apCost;
    }

    public void setApCost(Double apCost) {
        this.apCost = apCost;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsValue() {
        return isValue;
    }

    public void setIsValue(Integer isValue) {
        this.isValue = isValue;
    }

    public Double getStageEfficiency() {
        return stageEfficiency;
    }

    public void setStageEfficiency(Double stageEfficiency) {
        this.stageEfficiency = stageEfficiency;
    }

    public Integer getStageColor() {
        return stageColor;
    }

    public void setStageColor(Integer stageColor) {
        this.stageColor = stageColor;
    }

    @Override
    public String toString() {
        return "EfficiencyResult{" +
                "id=" + id +
                ", stageId='" + stageId + '\'' +
                ", sampleSize=" + sampleSize +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", stageCode='" + stageCode + '\'' +
                ", knockRating=" + knockRating +
                ", result=" + result +
                ", apExpect=" + apExpect +
                ", main='" + main + '\'' +
                ", mainLevel=" + mainLevel +
                ", itemType='" + itemType + '\'' +
                ", secondary='" + secondary + '\'' +
                ", apCost=" + apCost +
                ", isShow=" + isShow +
                ", isValue=" + isValue +
                ", stageEfficiency=" + stageEfficiency +
                '}';
    }
}
