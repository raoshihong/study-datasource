package com.daoyuan.study.datasource.dbs;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataSourceFactory {

    private static final String prefix = "spring.datasource";

    @Autowired
    private Environment env;//获取属性变量

    public DataSource build(String dbType){
        DruidDataSource druidDataSource = new DruidDataSource();
        String type = "."+dbType;
        if ("default".equals(dbType)) {
            type = "";
        }
        String url = env.getProperty(prefix+type+".url");
        String username = env.getProperty(prefix+type+".username");
        String password = env.getProperty(prefix+type+".password");
        String driverClassName = env.getProperty(prefix + type + ".driver-class-name");
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        return druidDataSource;
    }
}
