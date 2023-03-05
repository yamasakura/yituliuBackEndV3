package com.lhs.common.annotation;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TakeCountAspect {
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    //扫描所有添加了@TakeCount注解的方法
    @Before("@annotation(takeCount)")
    public void takeCountBefore(TakeCount takeCount){
        startTime.set(System.currentTimeMillis());
    }

    //接口方法执行完成之后
    @After("@annotation(takeCount)")
    public void takeCountAfter(TakeCount takeCount){
        log.info(takeCount.method()+"接口耗时："+(System.currentTimeMillis()-startTime.get())+"ms");
    }

}
