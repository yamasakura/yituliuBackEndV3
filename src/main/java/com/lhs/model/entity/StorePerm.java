package com.lhs.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("store_perm")
public class StorePerm {
    @TableId
    private Integer id;
    private String storeType;
    private String itemName;
    private Double cost;
    @TableField("cost_per")
    private Double costPer;
    private Integer quantity;
    private Integer rarity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCostPer() {
        return costPer;
    }

    public void setCostPer(Double costPer) {
        this.costPer = costPer;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getRarity() {
        return rarity;
    }

    public void setRarity(Integer rarity) {
        this.rarity = rarity;
    }

    @Override
    public String toString() {
        return "StorePerm{" +
                "id=" + id +
                ", storeType='" + storeType + '\'' +
                ", itemName='" + itemName + '\'' +
                ", cost=" + cost +
                ", costPer=" + costPer +
                ", quantity=" + quantity +
                ", rarity=" + rarity +
                '}';
    }
}
