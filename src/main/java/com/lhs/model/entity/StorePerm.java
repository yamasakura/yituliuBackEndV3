package com.lhs.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("store_perm")
public class StorePerm {
    @TableId
    private Integer id;
    private String storeType;
    private String itemName;
    private Double cost;
    private Double costPer;
    private Integer quantity;
    private Integer rarity;


}
