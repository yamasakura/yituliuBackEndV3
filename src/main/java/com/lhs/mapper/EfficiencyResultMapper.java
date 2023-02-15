package com.lhs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhs.model.entity.EfficiencyResult;
import com.lhs.model.entity.Item;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface EfficiencyResultMapper extends BaseMapper<EfficiencyResult> {
    @Update("truncate table efficiency_result")
    void deleteTableTemp();
}
