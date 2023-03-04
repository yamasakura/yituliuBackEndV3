package com.lhs.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhs.model.entity.StorePerm;

public interface StorePermService extends IService<StorePerm> {

    void updateStorePermDate();
}
