package com.daoyuan.study.datasource.controller;

import com.daoyuan.study.datasource.dbs.DataSourceFactory;
import com.daoyuan.study.datasource.dbs.DynamicDataSource;
import com.daoyuan.study.datasource.service.ChannelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ChannelsController {

    @Autowired
    private ChannelsService channelsService;

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @GetMapping("/save")
    public void save(){
        try {
            channelsService.saveA();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            channelsService.saveB();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String url = "jdbc:mysql://localhost:3306/db3";
            String username = "root";
            String password = "123456";
            String driverClass = "org.gjt.mm.mysql.Driver";
            DataSource dataSource = DataSourceFactory.buidDataSource(url,username,password,driverClass);

            Map<Object,Object> targetDataSources = new HashMap<>();
            targetDataSources.put("db3",dataSource);
            dynamicDataSource.addTargetDataSources(targetDataSources);
            channelsService.saveC();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
