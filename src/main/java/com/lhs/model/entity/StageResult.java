package com.lhs.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("stage_result")
public class StageResult implements Serializable {

    @TableId
    private Long id;
    private String stageId;  // 关卡id
    private String stageCode;   // 关卡名称
    private Double sampleConfidence; //置信度
    private Double spm;  //每分钟消耗理智
    private String zoneName;  //章节名称

    private String zoneId;  //区域Id
    private Integer isShow;   // 是否显示
    private Integer isValue;  //是否参与定价
    private Double apCost;  //理智消耗
    private String main; // 主材料
    private String secondary; // 副材料

    private String secondaryId; // 副材料id
    private String itemId;   //材料ID
    private String itemName;    //材料名称
    private String itemType;  //材料类型
    private Integer sampleSize;  // 样本次数
    private Double knockRating;   // 概率
    private Double apExpect; // 期望理智
    private Double result;   // 单项结果
    private Double stageEfficiency;    //理智转化率
    private Integer stageColor;   //关卡在前端显示的颜色




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

    public String getSecondaryId() {
        return secondaryId;
    }

    public void setSecondaryId(String secondaryId) {
        this.secondaryId = secondaryId;
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

    public Double getSpm() {
        return spm;
    }

    public void setSpm(Double spm) {
        this.spm = spm;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getSampleConfidence() {
        return sampleConfidence;
    }

    public void setSampleConfidence(Double sampleConfidence) {
        this.sampleConfidence = sampleConfidence;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public StageResult() {
    }


    @Override
    public String toString() {
        return "StageResult{" +
                "id=" + id +
                ", stageId='" + stageId + '\'' +
                ", stageCode='" + stageCode + '\'' +
                ", sampleConfidence=" + sampleConfidence +
                ", spm=" + spm +
                ", zoneName='" + zoneName + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", isShow=" + isShow +
                ", isValue=" + isValue +
                ", apCost=" + apCost +
                ", main='" + main + '\'' +
                ", secondary='" + secondary + '\'' +
                ", secondaryId='" + secondaryId + '\'' +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemType='" + itemType + '\'' +
                ", sampleSize=" + sampleSize +
                ", knockRating=" + knockRating +
                ", apExpect=" + apExpect +
                ", result=" + result +
                ", stageEfficiency=" + stageEfficiency +
                ", stageColor=" + stageColor +
                '}';
    }
}
