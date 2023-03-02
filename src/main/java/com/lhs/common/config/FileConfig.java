package com.lhs.common.config;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileConfig implements InitializingBean {

    @Value("${filePath.penguin}")
    private String penguin;  //    企鹅物流数据文件位置

    @Value("${filePath.item}")
    private String item;  //    材料相关数据文件位置

    public static String Penguin;
    public static String Item;

    @Override
    public void afterPropertiesSet() throws Exception {
        Penguin = penguin;
        Item = item;
    }
}
