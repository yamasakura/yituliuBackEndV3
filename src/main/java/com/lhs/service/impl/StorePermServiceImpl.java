package com.lhs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhs.mapper.StorePermMapper;
import com.lhs.model.entity.Item;
import com.lhs.model.entity.StorePerm;
import com.lhs.service.ItemService;
import com.lhs.service.StorePermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StorePermServiceImpl extends ServiceImpl<StorePermMapper, StorePerm> implements StorePermService {

    @Autowired
    private StorePermMapper storePermMapper;

    @Autowired
    private ItemService itemService;

    @Override
    public void updateStorePermDate() {

        List<StorePerm> storePerms = storePermMapper.selectList(null);
        Map<String, Item> collect = itemService.findAllItemValue(0.625).stream().collect(Collectors.toMap(Item::getItemName, Function.identity()));
        storePerms.forEach(storePerm -> {
            storePerm.setCostPer(collect.get(storePerm.getItemName()).getItemValue() / storePerm.getCost() / storePerm.getQuantity());
            if ("grey".equals(storePerm.getStoreType())) storePerm.setCostPer(storePerm.getCostPer()*100);
            storePerm.setRarity(collect.get(storePerm.getItemName()).getRarity());
            System.out.println(storePerm);
        });

        updateBatchById(storePerms);
    }


}
