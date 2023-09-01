package com.zhangjun.quyi.api_auto_test;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
// 开启注册中心
@EnableDiscoveryClient
// 开启远程调用,指定扫描远程接口包
@EnableFeignClients(basePackages = "com.zhangjun.quyi.api_auto_test.api")
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
@MapperScan(value = "com.zhangjun.quyi.api_auto_test.mapper")
public class ApiAutoTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAutoTestApplication.class,args);
    }
}
