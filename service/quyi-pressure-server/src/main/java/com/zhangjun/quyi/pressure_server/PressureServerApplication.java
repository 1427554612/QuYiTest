package com.zhangjun.quyi.pressure_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
public class PressureServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PressureServerApplication.class,args);
    }
}
