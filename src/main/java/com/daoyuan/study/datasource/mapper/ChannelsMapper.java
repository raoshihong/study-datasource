package com.daoyuan.study.datasource.mapper;

import com.daoyuan.study.datasource.model.Channels;

public interface ChannelsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Channels record);

    int insertSelective(Channels record);

    Channels selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Channels record);

    int updateByPrimaryKey(Channels record);
}