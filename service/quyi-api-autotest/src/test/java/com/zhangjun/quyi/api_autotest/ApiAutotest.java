package com.zhangjun.quyi.api_autotest;

import com.zhangjun.quyi.api_auto_test.ApiAutoTestApplication;
import com.zhangjun.quyi.api_auto_test.controller.ApiAutoTestController;
import com.zhangjun.quyi.api_auto_test.service.ApiAutoTestService;
import com.zhangjun.quyi.api_auto_test.service.impl.ApiAutoTestServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ApiAutoTestApplication.class)
public class ApiAutotest {

    @Autowired
    private ApiAutoTestService apiAutoTestService;

    @Test
    public void test(){
        System.out.println(apiAutoTestService);
    }

}
