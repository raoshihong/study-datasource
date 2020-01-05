package com.daoyuan.study.datasource.controller;

import com.daoyuan.study.datasource.dbs.DataSourceContextHolder;
import com.daoyuan.study.datasource.service.ChannelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChannelsController {

    @Autowired
    private ChannelsService channelsService;

    @GetMapping("/save")
    public void save(){
        try {
            DataSourceContextHolder.clear();
            DataSourceContextHolder.setDBAlais("db1");
            channelsService.saveA();

        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            DataSourceContextHolder.clear();
            DataSourceContextHolder.setDBAlais("db2");
            channelsService.saveB();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

//    @GetMapping("saveTransaction")
//    public void saveTransaction(){
//        channelsService.saveTransaction();
//    }
//
//    @GetMapping("saveTransaction1")
//    public void saveTransaction1(){
//        channelsService.saveTransaction1();
//    }
//
//    @GetMapping("saveTransaction2")
//    public void saveTransaction2(){
//        channelsService.saveTransaction2();
//    }
}
