package com.lhs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhs.model.entity.BuildingSchedule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleMapper  extends BaseMapper<BuildingSchedule> {

     List<BuildingSchedule> selectPage1(@Param("page")Integer page,@Param("size")Integer size);
}
