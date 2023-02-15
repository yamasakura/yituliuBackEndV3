package com.lhs.model.vo;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FilePath implements InitializingBean {

    @Value("${filePath.penguin}")
    private String penguin;

    @Value("${filePath.item}")
    private String item;

    public static String Penguin;
    public static String Item;
    @Override
    public void afterPropertiesSet() throws Exception {
        Penguin = penguin;
        Item = item;
    }
}
