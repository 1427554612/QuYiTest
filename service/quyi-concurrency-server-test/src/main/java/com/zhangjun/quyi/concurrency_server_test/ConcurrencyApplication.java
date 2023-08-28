package com.zhangjun.quyi.concurrency_server_test;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
// 开启注册中心
@EnableDiscoveryClient
// 开启远程调用,指定扫描远程接口包
@EnableFeignClients(basePackages = "com.zhangjun.quyi.concurrency_server_test.api")
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
public class ConcurrencyApplication {

    
}
