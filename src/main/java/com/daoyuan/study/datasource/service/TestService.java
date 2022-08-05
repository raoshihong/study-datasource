package com.daoyuan.study.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author raoshihong
 * @date 8/6/22 12:01 AM
 */
@Service
public class TestService {

    @Autowired
    private ChannelsService channelsService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Transactional(rollbackFor = Exception.class)
    public void test() {

        categoryService.saveCategory();

        channelsService.saveChannel();

//        productService.saveProduct();

        int i = 10/0;

    }
}
