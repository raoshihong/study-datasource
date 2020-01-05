package com.daoyuan.study.datasource.dbs;

import com.alibaba.druid.pool.DruidDataSource;
import com.daoyuan.study.datasource.entity.DataSourceConfig;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceFactory {

    private static List<DataSourceConfig> dataSourceConfigs;

    static {
        dataSourceConfigs = new ArrayList<>();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setAlias("db1");
        dataSourceConfig.setDbUrl("jdbc:mysql://localhost:3306/db1");
        dataSourceConfig.setDbUsername("root");
        dataSourceConfig.setDbPassword("123456");
        dataSourceConfig.setDbDriverClass("org.gjt.mm.mysql.Driver");
        dataSourceConfigs.add(dataSourceConfig);

        DataSourceConfig dataSourceConfig1 = new DataSourceConfig();
        dataSourceConfig1.setAlias("db2");
        dataSourceConfig1.setDbUrl("jdbc:mysql://localhost:3306/db2");
        dataSourceConfig1.setDbUsername("root");
        dataSourceConfig1.setDbPassword("123456");
        dataSourceConfig1.setDbDriverClass("org.gjt.mm.mysql.Driver");
        dataSourceConfigs.add(dataSourceConfig1);
    }

    public static DataSource buildDefault(){
        String url = "jdbc:mysql://localhost:3306/db1";
        String username = "root";
        String password = "123456";
        String driverClassName = "org.gjt.mm.mysql.Driver";
        return buidDataSource(url,username,password,driverClassName);
    }

    public static Map<Object,Object> buildTargetDataSources(){

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

    public static DataSource buidDataSource(String url,String username,String password,String driverClassName){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setValidationQuery("select 1");
        return druidDataSource;
    }
}
