package com.daoyuan.study.datasource.dbs;

import com.alibaba.druid.pool.DruidDataSource;
import com.daoyuan.study.datasource.entity.DataSourceConfig;
import com.daoyuan.study.datasource.mapper.DataSourceConfigMapper;
import com.daoyuan.study.datasource.utils.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataSourceFactory {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    public DataSource buildDefault(){
        String url = dataSourceProperties.getUrl();
        String username = dataSourceProperties.getUsername();
        String password = dataSourceProperties.getPassword();
        String driverClassName = dataSourceProperties.getDriverClassName();
        return buidDataSource(url,username,password,driverClassName);
    }

    public Map<Object,Object> buildTargetDataSources(){

        List<DataSourceConfig> dataSourceConfigs = new ArrayList<>();
        Map<Object,Object> targetDataSourceMap = new HashMap<Object, Object>();

        dataSourceConfigs.stream().forEach(dataSourceConfig -> {

            String url = dataSourceConfig.getDbUrl();
            String username = dataSourceConfig.getDbUsername();
            String password = dataSourceConfig.getDbPassword();
            String driverClassName = dataSourceConfig.getDbDriverClass();
            DataSource dataSource = buidDataSource(url,username,password,driverClassName);

            targetDataSourceMap.put(dataSourceConfig.getAlias(),dataSource);
        });

        return targetDataSourceMap;
    }

    public DataSource buidDataSource(String url,String username,String password,String driverClassName){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 1");
        return druidDataSource;
    }
}
