package com.daoyuan.study.datasource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author raoshihong
 * @date 8/5/22 11:52 PM
 */
@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
}
