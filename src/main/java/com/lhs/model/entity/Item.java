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

    private String  type; //物品稀有度

    private Integer rarity; //物品稀有度

    private Integer cardNum;  //前端排序的用索引

    private Double expCoefficient;  //经验书系数

    private Double weight;   //加工站爆率

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

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemValue=" + itemValue +
                ", type='" + type + '\'' +
                ", rarity=" + rarity +
                ", cardNum=" + cardNum +
                ", expCoefficient=" + expCoefficient +
                ", weight=" + weight +
                '}';
    }
}
