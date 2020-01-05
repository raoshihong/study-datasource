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
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties({MybatisProperties.class})//开启Mybatis配置属性
@MapperScan("com.daoyuan.study.datasource.mapper")
public class DynamicDataSourceConfig {

    private MybatisProperties mybatisProperties;

    public DynamicDataSourceConfig(MybatisProperties mybatisProperties) {//自动注入MybatisProperties相关属性
        this.mybatisProperties = mybatisProperties;
    }

    //创建一个数据源
    @Bean(name = "dynamicDataSource")
    @Primary
    public DataSource dynamicDataSource(@Autowired DataSourceFactory dataSourceFactory){
        //创建多个数据源,要有一个默认的数据,如果没有获取到登录信息，则可以认为是默认数据源
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.addTargetDataSources(dataSourceFactory.buildTargetDataSources());//设置其他目标数据
        dynamicDataSource.setDefaultTargetDataSource(dataSourceFactory.buildDefault());//设置默认数据

        return dynamicDataSource;
    }

    //将数据源注入到sqlSessionFactory中
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setMapperLocations(mybatisProperties.resolveMapperLocations());
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());

        return sqlSessionFactoryBean.getObject();
    }

}
