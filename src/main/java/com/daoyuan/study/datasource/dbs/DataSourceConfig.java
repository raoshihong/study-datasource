package com.daoyuan.study.datasource.dbs;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties({MybatisProperties.class})//开启Mybatis配置属性
@MapperScan("com.daoyuan.study.datasource.mapper")
public class DataSourceConfig {

    private MybatisProperties mybatisProperties;

    public DataSourceConfig(MybatisProperties mybatisProperties) {//自动注入MybatisProperties相关属性
        this.mybatisProperties = mybatisProperties;
    }

    @Bean(name = "dynamicDataSource")
    public DataSource dynamicDataSource(@Autowired DataSourceFactory dataSourceFactory){
        //创建多个数据源
        DataSource dbs = dataSourceFactory.build("default");
        DataSource dbs1 = dataSourceFactory.build("dbs1");
        Map<Object,Object> targetDataSourceMap = new HashMap<Object, Object>();
        targetDataSourceMap.put("default",dbs);
        targetDataSourceMap.put("dbs1",dbs1);

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.addTargetDataSources(targetDataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(dbs);

        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());

        return sqlSessionFactoryBean.getObject();
    }

}
