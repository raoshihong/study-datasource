package com.daoyuan.study.datasource.dbs;

import com.daoyuan.study.datasource.service.AppCodeService;
import com.daoyuan.study.datasource.utils.SpringContext;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class DataSourceWebProxy extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        DynamicDataSourceFactory dataSourceFactory = SpringContext.getBean(DynamicDataSourceFactory.class);
        //appCode交给用户自定义
        AppCodeService appCodeService = SpringContext.getBean(AppCodeService.class);
        String appCode = "";
        if (Objects.nonNull(appCodeService)) {
            appCode = appCodeService.getAppCode();
        }

        String alias = dataSourceFactory.getAppCodeAliasMapping().get(appCode);

        if (StringUtils.isEmpty(alias)) {
            alias = "db1";
        }
        DataSourceContextHolder.setDBAlais(alias);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        DataSourceContextHolder.clear();
    }
}
