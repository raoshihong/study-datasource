package com.daoyuan.study.datasource.dbs;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DataSourceWebProxy extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String alias = request.getHeader("alias");
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
