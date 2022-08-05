package com.daoyuan.study.datasource.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.daoyuan.study.datasource.mapper")
public class CoreConfig {

}
