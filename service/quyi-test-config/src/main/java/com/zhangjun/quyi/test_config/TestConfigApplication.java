package com.zhangjun.quyi.test_config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
public class TestConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestConfigApplication.class,args);
    }
}
