package com.zhangjun.quyi.concurrency_server_test.util;

import com.zhangjun.quyi.concurrency_server_test.task.ConcurrencyTask;

import java.util.concurrent.*;

public class ExceutorsUtil {
    
    private static  ExecutorService executorService = null;
    private static CountDownLatch countDownLatch;

    /**
     * 初始化线程池
     * @param requestNumber
     */
    public static void init(int requestNumber){
        if (executorService== null)executorService = Executors.newFixedThreadPool(requestNumber);
        countDownLatch = new CountDownLatch(requestNumber);
    }

    // ConcurrencyTask task
    private static Future<ConcurrencyTask> run() throws Exception {
        CompletableFuture<String> resultFuture = null;
        for (int i = 0; i < 10; i++) {
            resultFuture = CompletableFuture.supplyAsync(() -> {
                String name = Thread.currentThread().getName();
                return name;
            }, executorService);
        }
        System.out.println(resultFuture.get());
        return null;
    }

    public static void main(String[] args) throws Exception {
        init(10);
        run();
    }


}
