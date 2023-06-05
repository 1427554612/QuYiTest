package com.zhangjun.quyi.pressure_client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
public class PressureClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(PressureClientApplication.class,args);
    }
}
