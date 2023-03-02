package com.lhs.service;

import com.baomidou.mybatisplus.extension.service.IService;


import com.lhs.model.entity.Stage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


public interface StageService extends IService<Stage> {

    void importStageData(MultipartFile file);
    //导出关卡信息excel
    void exportStageData(HttpServletResponse response);
    //更新关卡信息
    void updateStageInfo(String stageId);
}
