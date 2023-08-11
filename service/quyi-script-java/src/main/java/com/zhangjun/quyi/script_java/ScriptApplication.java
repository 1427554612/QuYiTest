package com.zhangjun.quyi.script_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
@EnableDiscoveryClient
public class ScriptApplication {
    public static void main(String[] args) {
        SpringApplication.run(ScriptApplication.class,args);
    }
}
