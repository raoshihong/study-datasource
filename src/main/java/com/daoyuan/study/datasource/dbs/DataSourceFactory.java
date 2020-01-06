package com.daoyuan.study.datasource.dbs;

import com.alibaba.druid.pool.DruidDataSource;
import com.daoyuan.study.datasource.entity.DataSourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class DataSourceFactory {

    @Autowired
    private AbstractEnvironment environment;

    public DataSource buildDefaultDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.configFromPropety(getProperties());
        return druidDataSource;
    }

    public Map<Object,Object> buildTargetDataSources(List<DataSourceConfig> dataSourceConfigs){

        Map<Object,Object> targetDataSourceMap = new HashMap<Object, Object>();
        dataSourceConfigs.stream().forEach(dataSourceConfig -> {

            String url = dataSourceConfig.getDbUrl();
            String username = dataSourceConfig.getDbUsername();
            String password = dataSourceConfig.getDbPassword();
            String driverClassName = dataSourceConfig.getDbDriverClass();
            DataSource dataSource = buildTargetDataSource(url,username,password,driverClassName);

            targetDataSourceMap.put(dataSourceConfig.getAlias(),dataSource);
        });

        return targetDataSourceMap;
    }

    private Properties getProperties(){

        RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
        Map<String,Object> map = resolver.getSubProperties("spring.datasource.");

        Properties properties = new Properties();

        map.forEach((key, value) -> {
            properties.put(key,value);
        });
        return properties;
    }

    public DataSource buildTargetDataSource(String url,String username,String password,String driverClassName){

        DruidDataSource druidDataSource = new DruidDataSource();

        druidDataSource.configFromPropety(getProperties());

        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);

        return druidDataSource;
    }
}
