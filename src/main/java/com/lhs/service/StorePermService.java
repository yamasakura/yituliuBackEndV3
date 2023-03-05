package com.lhs.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.lhs.model.entity.StorePerm;

public interface StorePermService extends IService<StorePerm> {
    /**
     * 更新常驻商店性价比数据
     */
    void updateStorePermDate();
}
