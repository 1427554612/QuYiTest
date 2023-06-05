package com.zhangjun.quyi.test_task;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务模块
 */
@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.zhangjun.quyi"})
@EnableFeignClients(basePackages = "com.zhangjun.quyi.test_task.api")
public class TaskApplication {
    public static void main(String[] args) {
        // 问题的原因就是resources文件夹没有打入jar，解决这个应该就没问题了。ok、我试试
        SpringApplication.run(TaskApplication.class,args);
    }
}
