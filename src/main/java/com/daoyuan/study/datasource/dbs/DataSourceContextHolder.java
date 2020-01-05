package com.daoyuan.study.datasource.dbs;

/**
 * 用来记录当前线程中使用的是哪个数据源，不过有时候同一线程可能需要切换不同数据源（多个方法调用,不同方法中使用不同数据,此时需要先清除再设置当前数据源）
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHold = new ThreadLocal<String>();

    //返回当前线程的数据源
    public static String getDBAlias(){
        return contextHold.get();
    }

    //设置当前线程中的数据源
    public static void setDBAlais(String dbAlais){
        contextHold.set(dbAlais);
    }

    //清除当前线程中的数据
    public static void clear(){
        contextHold.remove();
    }
}
