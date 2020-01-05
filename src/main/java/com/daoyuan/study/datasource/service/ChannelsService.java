package com.daoyuan.study.datasource.service;

import com.daoyuan.study.datasource.dbs.DataSourceContextHolder;
import com.daoyuan.study.datasource.entity.Channels;
import com.daoyuan.study.datasource.mapper.ChannelsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChannelsService {
    @Autowired
    private ChannelsMapper channelsMapper;


//    @Autowired
//    private DynamicDataSource dynamicDataSource;

//    @Autowired
//    private DataSourceFactory dataSourceFactory;

    /**
     * 这种没有指定事务的话,是可以动态切换不同数据源的,但是如果发生错误，无法进行事务回滚
     */
//    @Transactional
//    public void save(){
//        //使用默认的数据源
//        Channels channels = new Channels();
//        channels.setName("aa");
//        channels.setAppCode("1");
//        channels.setLevelType("a");
//        channelsMapper.insert(channels);
//
//        DataSourceContextHolder.clear();
//        DataSourceContextHolder.setDBAlais("db2");
//
//        channels.setName("bb");
//        channels.setAppCode("2");
//        channels.setLevelType("b");
//        channelsMapper.insert(channels);
//    }

    @Transactional
    public void saveA(){
        //使用默认的数据源
        Channels channels = new Channels();
        channels.setName("aa");
        channels.setAppCode("1");
        channels.setLevelType("a");
        channelsMapper.insert(channels);
        int i=10/0;
    }

    @Transactional
    public void saveB(){
        Channels channels = new Channels();

        channels.setName("bb");
        channels.setAppCode("2");
        channels.setLevelType("b");
        channelsMapper.insert(channels);
        int i=10/0;
    }

}
