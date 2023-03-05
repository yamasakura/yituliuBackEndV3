package com.lhs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhs.common.config.FileConfig;
import com.lhs.common.util.FileUtil;
import com.lhs.mapper.ItemMapper;
import com.lhs.mapper.StageMapper;
import com.lhs.model.entity.Item;
import com.lhs.model.entity.Stage;
import com.lhs.model.entity.StageResult;
import com.lhs.model.vo.PenguinDataResponseVo;
import com.lhs.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StageServiceImpl extends ServiceImpl<StageMapper, Stage> implements StageService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private StageMapper stageMapper;


    @Override
    public List<Stage> findAll(QueryWrapper<Stage> queryWrapper) {
        return stageMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void importStageData(MultipartFile file) {
        List<Stage> list = new ArrayList<>();
        Map<String, Item> itemMap = itemMapper.selectList(null).stream().collect(Collectors.toMap(Item::getItemName, Function.identity()));
        JSONObject itemType_table = JSONObject.parseObject(FileUtil.read(FileConfig.Item + "itemType_table.json"));
        JSONObject stageZone_table = JSONObject.parseObject(FileUtil.read(FileConfig.Item + "stageZone_table.json"));


        try {
            EasyExcel.read(file.getInputStream(), Stage.class, new AnalysisEventListener<Stage>() {
                @Override
                public void invoke(Stage stage, AnalysisContext analysisContext) {
                    try {
                        if (!"0".equals(stage.getMain())) stage.setMainRarity(itemMap.get(stage.getMain()).getRarity());
                        if (!"0".equals(stage.getMain())) stage.setItemType(itemType_table.getString(stage.getMain()));
                        stage.setZoneName(stageZone_table.getString(stage.getZoneId()));
                        stage.setSpm(stage.getApCost() / stage.getMinClearTime() * 60000);
                        if (!"0".equals(stage.getSecondary()))
                            stage.setSecondaryId(itemMap.get(stage.getSecondary()).getItemId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(1);
                    if (stage.getZoneId().startsWith("act")) {
                        stage.setStageState(1);
                        stage.setIsValue(0);
                        stage.setIsShow(0);
                        if (stage.getZoneId().endsWith("perm")) {
                            stage.setIsShow(1);
                            stage.setIsValue(1);
                        }
                    } else {
                        stage.setIsValue(1);
                        stage.setIsShow(1);
                        stage.setStageState(0);
                    }

                    System.out.println(stage);
                    list.add(stage);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                }
            }).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        saveOrUpdateBatch(list);

    }

    @Override
    public void exportStageData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("stage", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            List<Stage> list = stageMapper.selectList(null);

            EasyExcel.write(response.getOutputStream(), Stage.class).sheet("Sheet1").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStageInfo(String stageId) {

    }




    //复制材料表和关卡表的一些信息
    private static void stageResultVo(StageResult efficiencyResult, Stage stage, Item item) {

    }
}
