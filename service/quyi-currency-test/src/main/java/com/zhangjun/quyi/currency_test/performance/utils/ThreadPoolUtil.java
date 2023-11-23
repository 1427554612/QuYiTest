package com.zhangjun.quyi.currency_test.performance.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolUtil {

    public static Integer THREAD_NUMBER_TIMES = 10;          // 默认倍数
    public static ExecutorService executors = null;           // 线程池
    public static volatile CountDownLatch countDownLatch = null;      // 计数器
    public static volatile List<Future> futureList = Collections.synchronizedList(new ArrayList<>());  // 同步任务集合

    /**
     * 初始化线程池
     * @param requestNumber：请求数
     */
    public static void initThreadPool(int requestNumber){
        if (executors==null) ThreadPoolUtil.executors =
                Executors.newFixedThreadPool(ThreadPoolUtil.getThreadNumber(requestNumber));
        ThreadPoolUtil.setCountDownLatch(requestNumber);
        if (futureList == null) futureList = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * 设置计数器
     * @param maxRequest
     * @return
     */
    private static CountDownLatch setCountDownLatch(Integer maxRequest){
        ThreadPoolUtil.countDownLatch  =  new CountDownLatch(maxRequest);
        return ThreadPoolUtil.countDownLatch;
    }

    /**
     * 计算线程池中线程创建个数
     * @param requestNumber：请求数
     * @return
     */
    public static int getThreadNumber(int requestNumber){
        int finalThreadNumber = 0;
        if(requestNumber<THREAD_NUMBER_TIMES) finalThreadNumber = requestNumber;
        else if (requestNumber>=THREAD_NUMBER_TIMES){
            int tempNumber = requestNumber % THREAD_NUMBER_TIMES;
            if (tempNumber!=0) finalThreadNumber = requestNumber / THREAD_NUMBER_TIMES - tempNumber;
            else finalThreadNumber = requestNumber / THREAD_NUMBER_TIMES;
        }
        System.out.println("线程池线程数量：" + finalThreadNumber);
        return finalThreadNumber;
    }

    /**
     * 执行线程任务
     * @param requestNumber
     */
    public static void start(int requestNumber, Runnable runnable) throws Exception {
        initThreadPool(requestNumber);
        System.out.println(executors);
        for (int i = 0 ; i<requestNumber;++i){
            executors.submit(runnable);
        }
        ThreadPoolUtil.countDownLatch.await();
    }



}
