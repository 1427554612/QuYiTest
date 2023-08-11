package com.zhangjun.quyi.script_java.script_sty.thread_demo;

import com.zhangjun.quyi.script_java.script_sty.regex_demo.RegexDemo;
import com.zhangjun.quyi.utils.RequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 并发发送自己的saveConfig接口
 */
public class ApiThreadEntity implements Callable {
    String url;
    String body;
    static CountDownLatch countDownLatch;
    static List<Future> futureList = new ArrayList<>();

    ApiThreadEntity(String url,String body){
        this.url = url;
        this.body = body;
    }


    @Override
    public Object call() throws Exception {
        if (this.body.contains("@")){
            this.body = RegexDemo.replace("@(.*?)@", body);
        }
        long st = System.currentTimeMillis();
        String s = null;
        s = runApi(url, body);
        System.out.println(s);
        int i = 10/0;
        countDownLatch.countDown();
        return Thread.currentThread().getName() + ", is run"  + ", time = " + (System.currentTimeMillis() - st + ",response = " + s);
    }



    public static String runApi(String url,String body) throws Exception {
        return RequestUtil.sendPost(url, body);
    }


    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        int Number = 1000;
        ApiThreadEntity.countDownLatch = new CountDownLatch(Number);
        RequestUtil.setOkhttpClient(Number,10000);
        ExecutorService executorService = Executors.newFixedThreadPool(Number/20);
        String url = "https://api.philucky.com/api/v1/coupon/number/use";
        String body = "{\n" +
                "    \"number\": \"20230810test\",\n" +
                "    \"token\": \"8aa4fd1a448140ee96c418de642495ee\",\n" +
                "    \"user_id\": \"64a6821aa97a93c9568806de\"\n" +
                "}";
        for (int i = 0; i < Number; i++) {
            futureList.add(executorService.submit(new ApiThreadEntity(url,body)));
        }
        countDownLatch.await();
        long e = System.currentTimeMillis();
//        for (Future future : futureList) {
//            System.out.println(future.get());
//        }
        System.out.println("总耗时:" + (e -l));
        executorService.shutdownNow();
    }
}
