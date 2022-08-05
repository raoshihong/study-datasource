package com.daoyuan.study.datasource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.study.datasource.datasource.annotation.DataSource;
import com.daoyuan.study.datasource.entity.Channels;
import com.daoyuan.study.datasource.mapper.ChannelsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataSource("db1")
@Service
public class ChannelsService extends ServiceImpl<ChannelsMapper, Channels> {


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveChannel(){
        Channels channels = new Channels();
        channels.setName("c1");
        this.save(channels);
    }

}
