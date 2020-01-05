package com.daoyuan.study.datasource.config;

import com.daoyuan.study.datasource.dbs.DataSourceWebProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CoreConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        super.addInterceptors(registry);
        registry.addInterceptor(new DataSourceWebProxy()).addPathPatterns("/**");
    }
}
