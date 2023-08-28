package com.zhangjun.quyi.concurrency_server_test.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 任务类
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ConcurrencyTask implements Callable {
    private String name;
    private Integer age;

    @Override
    public Object call() throws Exception {
        for (int i = 0; i < 10000; i++) {
            
        }
        return Thread.currentThread().getName() + ",name = " + this.name + ",age = " +this.age;
    }

    public static void main(String[] args) {
    }
}
