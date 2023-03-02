package com.lhs.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhs.model.entity.Item;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ItemMapper extends BaseMapper<Item> {

    @Update("truncate table item")
    void deleteTableTemp();
}
