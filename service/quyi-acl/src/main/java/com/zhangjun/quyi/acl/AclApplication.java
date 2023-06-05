package com.zhangjun.quyi.acl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class AclApplication {

    public static void main(String[] args) {
        SpringApplication.run(AclApplication.class,args);
    }
}
