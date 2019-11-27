package com.daoyuan.study.datasource.dbs;

/**
 * 用来记录当前线程中使用的是哪个数据源，不过有时候同一线程可能需要切换不同数据源
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHold = new ThreadLocal<String>();

    public static String getDbType(){
        return contextHold.get();
    }

    public static void setDbType(String dbType){
        contextHold.set(dbType);
    }

    public static void clear(){
        contextHold.remove();
    }
}
