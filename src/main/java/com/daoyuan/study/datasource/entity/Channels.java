package com.daoyuan.study.datasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@TableName("channels")
public class Channels {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
}