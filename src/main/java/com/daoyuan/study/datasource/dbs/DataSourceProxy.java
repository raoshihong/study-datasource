package com.daoyuan.study.datasource.dbs;

import com.daoyuan.study.datasource.annotation.NeedDataSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1) //设置为1,让该切面在@Transaction开启事物前执行
@Aspect
@Component
public class DataSourceProxy {

    @Before("@annotation(needDataSource)")
    public void before(JoinPoint joinPoint, NeedDataSource needDataSource){
        //在方法前,且需要在事物开启前设置数据源
        DataSourceContextHolder.setDBAlais(needDataSource.alias());
    }

    @After("@annotation(needDataSource)")
    public void after(JoinPoint joinPoint, NeedDataSource needDataSource){
        DataSourceContextHolder.clear();
    }

}
