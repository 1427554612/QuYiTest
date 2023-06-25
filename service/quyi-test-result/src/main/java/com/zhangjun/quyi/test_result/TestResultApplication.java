package com.zhangjun.quyi.test_result;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication()
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
@EnableFeignClients(basePackages = "com.zhangjun.quyi.test_result.api")
public class TestResultApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestResultApplication.class,args);
    }
}
