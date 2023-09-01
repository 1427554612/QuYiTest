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
    public Object call(){
        long st = 0l;
        String response = "";
        try {
            if (this.body.contains("@")){
                this.body = RegexDemo.replace("@(.*?)@", body);
            }
            st = System.currentTimeMillis();
            response = runApi(url, body);
//            int i = 10/0;
            countDownLatch.countDown();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Thread.currentThread().getName() + ", is run"  + ", time = " + (System.currentTimeMillis() - st + ",response = " + response);
    }



    public static String runApi(String url,String body) throws Exception {
        return RequestUtil.sendPost(url, body);
    }


    public static void main(String[] args) throws Exception {
        int Number = 500;
        ApiThreadEntity.countDownLatch = new CountDownLatch(Number);
        RequestUtil.setOkhttpClient(Number,10000);
        ScheduledExecutorService  executorService = Executors.newScheduledThreadPool(Number/10);
        long l = System.currentTimeMillis();
        String url = "http://localhost:8000/api/test_config/saveTestConfig";
        String body = "{\n" +
                "  \"configData\": {},\n" +
                "  \"configMark\": \"11ddasda\",\n" +
                "  \"configName\": \"@thread@_@uuid@\",\n" +
                "  \"configType\": \"string\",\n" +
                "  \"updateUp\": \"张军\"\n" +
                "}";
        for (int i = 0; i < Number; i++) {
            futureList.add(executorService.schedule(new ApiThreadEntity(url,body),0,TimeUnit.SECONDS));
        }
        countDownLatch.await();
        long e = System.currentTimeMillis();
        System.out.println("总耗时:" + (e -l));
        for (Future future : futureList) {
            System.out.println(future.get());
        }
        executorService.shutdownNow();

        ThreadGroup threadGroup = new ThreadGroup("root");

    }
}
