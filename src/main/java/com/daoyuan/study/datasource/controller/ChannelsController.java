package com.daoyuan.study.datasource.controller;

import com.daoyuan.study.datasource.dbs.DataSourceContextHolder;
import com.daoyuan.study.datasource.dbs.DynamicDataSourceFactory;
import com.daoyuan.study.datasource.dbs.DynamicDataSource;
import com.daoyuan.study.datasource.mapper.DataSourceConfigMapper;
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

    @Autowired
    private DynamicDataSourceFactory dataSourceFactory;

    @Autowired
    private DataSourceConfigMapper dataSourceConfigMapper;

    @GetMapping("/save")
    public void save(){
        try {
            channelsService.saveA();
        }catch (Exception e){
            e.printStackTrace();
        }

        Map<Object,Object> targetDataSources = dataSourceFactory.buildTargetDataSources(dataSourceConfigMapper.selectList());
        dynamicDataSource.addTargetDataSources(targetDataSources);
        try {
            DataSourceContextHolder.setDBAlais("db2");
            channelsService.saveB();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            String url = "jdbc:mysql://localhost:3306/db3";
            String username = "root";
            String password = "123456";
            String driverClass = "org.gjt.mm.mysql.Driver";
            DataSource dataSource = dataSourceFactory.buildTargetDataSource(url,username,password,driverClass);

            targetDataSources = new HashMap<>();
            targetDataSources.put("db3",dataSource);
            dynamicDataSource.addTargetDataSources(targetDataSources);
            DataSourceContextHolder.setDBAlais("db3");
            channelsService.saveC();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
