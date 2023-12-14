package com.zhangjun.quyi.ftp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.zhangjun.quyi"})
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FtpApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtpApplication.class,args);
    }
}
