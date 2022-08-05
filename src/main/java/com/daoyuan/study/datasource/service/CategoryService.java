package com.daoyuan.study.datasource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.study.datasource.datasource.annotation.DataSource;
import com.daoyuan.study.datasource.entity.Category;
import com.daoyuan.study.datasource.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

/**
 * @author raoshihong
 * @date 8/6/22 12:15 AM
 */
@DataSource("db1")
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public void saveCategory(){
        Category category = new Category();
        category.setName("ct1");
        this.save(category);
    }
}
