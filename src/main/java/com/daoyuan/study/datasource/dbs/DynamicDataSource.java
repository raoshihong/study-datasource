package com.daoyuan.study.datasource.dbs;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源的添加和获取的类
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 用于保存已加载的所有动态数据源,供动态添加使用
     */
    private Map<Object,Object> dataSources = new HashMap<Object, Object>();

    /**
     * 添加targetDataSources
     * @param targetDataSources
     */
    public void addTargetDataSources(Map<Object, Object> targetDataSources) {
        dataSources.putAll(targetDataSources);
        this.setTargetDataSources(dataSources);
        this.afterPropertiesSet();//需要更新resolvedDataSources或者resolvedDefaultDataSource
    }

    /**
     *  这个方法的调用流程
     *  AbstractRoutingDataSource.getConnection() -> determineTargetDataSource() -> determineCurrentLookupKey()
     *  所以在获取连接时返回所需要的数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        //每次都从当前线程中获取对应的数据源对象
        return DataSourceContextHolder.getDbType();
    }

}
