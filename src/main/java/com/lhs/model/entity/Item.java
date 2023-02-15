package com.lhs.model.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("item")   //用于存储最终的等效理智/绿票价值
public class Item {

    @TableId
    private Long id;

    private String itemId;  //物品id


    private String itemName; //物品名称

    private Double itemValue; //物品价值

    private String type; //物品稀有度

    private Integer cardNum;  //前端排序的用索引

    private Double expCoefficient;  //经验书系数

    private Double workShopKnockRating;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    public Double getExpCoefficient() {
        return expCoefficient;
    }

    public void setExpCoefficient(Double expCoefficient) {
        this.expCoefficient = expCoefficient;
    }

    public Double getWorkShopKnockRating() {
        return workShopKnockRating;
    }

    public void setWorkShopKnockRating(Double workShopKnockRating) {
        this.workShopKnockRating = workShopKnockRating;
    }

    @Override
    public String toString() {
        return "ItemRevise{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemValue=" + itemValue +
                ", type='" + type + '\'' +
                ", cardNum=" + cardNum +
                ", expCoefficient=" + expCoefficient +
                '}';
    }
}
