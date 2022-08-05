package com.daoyuan.study.datasource.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daoyuan.study.datasource.datasource.annotation.DataSource;
import com.daoyuan.study.datasource.entity.Product;
import com.daoyuan.study.datasource.mapper.ProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author raoshihong
 * @date 8/5/22 11:54 PM
 */
@DataSource("db2")
@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {


    @Transactional(propagation = Propagation.REQUIRES_NEW , rollbackFor = Exception.class)
    public void saveProduct() {

        Product product = new Product();
        product.setName("p1");
        this.save(product);
    }
}
