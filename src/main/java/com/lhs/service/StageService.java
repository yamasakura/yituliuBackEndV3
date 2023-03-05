package com.lhs.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;


import com.lhs.model.entity.Stage;
import com.lhs.model.vo.PenguinDataResponseVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface StageService extends IService<Stage> {
    /**
     * 查找全部关卡数据
     * @return
     */
    List<Stage> findAll(QueryWrapper<Stage> queryWrapper);

    /**
     * 导入关卡信息文件
     * @param file  导入的文件，是个excel格式的
     */
    void importStageData(MultipartFile file);

    /**
     * 导出关卡信息数据
     * @param response  文件的响应体
     */
    void exportStageData(HttpServletResponse response);
    //更新关卡信息
    void updateStageInfo(String stageId);


}
