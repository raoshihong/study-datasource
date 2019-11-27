package com.daoyuan.study.datasource.service;

import com.daoyuan.study.datasource.dbs.DataSourceContextHolder;
import com.daoyuan.study.datasource.dbs.DataSourceFactory;
import com.daoyuan.study.datasource.dbs.DynamicDataSource;
import com.daoyuan.study.datasource.mapper.ChannelsMapper;
import com.daoyuan.study.datasource.model.Channels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChannelsService {
    @Autowired
    private ChannelsMapper channelsMapper;

    @Autowired
    private DynamicDataSource dynamicDataSource;

    @Autowired
    private DataSourceFactory dataSourceFactory;

    /**
     * 这种没有指定事务的话,是可以动态切换不同数据源的,但是如果发生错误，无法进行事务回滚
     */
    public void save(){
        //使用默认的数据源
        Channels channels = new Channels();
        channels.setName("sssss");
        channelsMapper.insertSelective(channels);

        //重新设置当前线程的数据源
        DataSourceContextHolder.setDbType("dbs1");

        channels.setName("aaaa");
        channelsMapper.insertSelective(channels);


        //创建新的数据源,动态添加新的数据源
        DataSource dataSource = dataSourceFactory.build("dbs2");

        Map<Object,Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("dbs2",dataSource);
        dynamicDataSource.addTargetDataSources(targetDataSources);

        //再重新设置当前线程的数据源
        DataSourceContextHolder.setDbType("dbs2");
        channels.setName("bbbb");
        channelsMapper.insertSelective(channels);
    }

    /**
     * 这种使用数据源
     */
    @Transactional
    public void saveTransaction(){
        //使用默认的数据源
        Channels channels = new Channels();
        channels.setName("sssss");
        channelsMapper.insertSelective(channels);

        //重新设置当前线程的数据源
        DataSourceContextHolder.setDbType("dbs1");

        channels.setName("aaaa");
        channelsMapper.insertSelective(channels);


        //创建新的数据源,动态添加新的数据源
        DataSource dataSource = dataSourceFactory.build("dbs2");

        Map<Object,Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("dbs2",dataSource);
        dynamicDataSource.addTargetDataSources(targetDataSources);

        //再重新设置当前线程的数据源
        DataSourceContextHolder.setDbType("dbs2");
        channels.setName("bbbb");
        channelsMapper.insertSelective(channels);
    }

    @Transactional
    public void saveTransaction1(){
        //使用默认的数据源
        Channels channels = new Channels();
        channels.setName("sssss");
        channelsMapper.insertSelective(channels);

        int i = 10/0;
    }

    @Transactional
    public void saveTransaction2(){
        //使用默认的数据源
        Channels channels = new Channels();
        channels.setName("sssss");
        channelsMapper.insertSelective(channels);

        //重新设置当前线程的数据源
        DataSourceContextHolder.setDbType("dbs1");

        channels.setName("aaaa");
        channelsMapper.insertSelective(channels);

        int i = 10/0;
    }
}
