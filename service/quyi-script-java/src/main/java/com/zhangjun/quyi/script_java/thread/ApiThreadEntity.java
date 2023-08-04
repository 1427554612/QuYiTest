package com.zhangjun.quyi.script_java.thread;

import com.zhangjun.quyi.script_java.regex.RegexDemo;
import com.zhangjun.quyi.utils.RequestUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        if (this.body.contains("@")){
            this.body = RegexDemo.replace("@(.*?)@", body);
        }
        System.out.println(this.body);
        long st = System.currentTimeMillis();
        String s = null;
        try {
            s = runApi(url, body);
            System.out.println(s);
        }catch (Exception e){
            System.out.println(e);
        }
        countDownLatch.countDown();
        return Thread.currentThread().getName() + ", is run"  + ", time = " + (System.currentTimeMillis() - st + ",response = " + s);
    }



    public static String runApi(String url,String body) throws Exception {
        return RequestUtil.sendPost(url, body);
    }


    public static void main(String[] args) throws Exception {
        long l = System.currentTimeMillis();
        int Number = 100;
        ApiThreadEntity.countDownLatch = new CountDownLatch(Number);
        RequestUtil.setOkhttpClient(Number,10000);
        ExecutorService executorService = Executors.newFixedThreadPool(Number/20);
        String url = "http://192.168.5.213:8002/api/test_config/saveTestConfig";
        String body = "{\n" +
                "  \"configData\": {\"demo\":\"test\"},\n" +
                "  \"configMark\": \"string\",\n" +
                "  \"configName\": \"@thread@_@uuid@_@time@\",\n" +
                "  \"configType\": \"api\",\n" +
                "  \"updateUp\": \"张军\"\n" +
                "}";
        for (int i = 0; i < Number; i++) {
            futureList.add(executorService.submit(new ApiThreadEntity(url,body)));
        }
        countDownLatch.await();
        long e = System.currentTimeMillis();
        for (Future future : futureList) {
            System.out.println(future.get());
        }
        System.out.println("总耗时:" + (e -l));
        executorService.shutdownNow();
    }
}
