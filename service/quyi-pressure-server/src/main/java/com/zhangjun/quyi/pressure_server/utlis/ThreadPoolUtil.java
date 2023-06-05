package com.zhangjun.quyi.pressure_server.utlis;

import com.zhangjun.quyi.pressure_server.entity.vo.ApiRunVo;
import com.zhangjun.quyi.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolUtil {

    private static final Integer THREAD_NUMBER_TIMES = 20;    // 默认倍数
    public static ExecutorService executors = null;           // 线程池
    public static volatile CountDownLatch countDownLatch = null;      // 计数器
    public static volatile List<Future> futureList = Collections.synchronizedList(new ArrayList<>());  // 同步任务集合

    /**
     * 初始化线程池
     * @param requestNumber：请求数
     */
    private static void initThreadPool(int requestNumber){
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
    private static int getThreadNumber(int requestNumber){
        int finalThreadNumber = 0;
        if(requestNumber<THREAD_NUMBER_TIMES) finalThreadNumber = requestNumber;
        else if (requestNumber>=THREAD_NUMBER_TIMES){
            int tempNumber = requestNumber % THREAD_NUMBER_TIMES;
            if (tempNumber!=0) finalThreadNumber = requestNumber / THREAD_NUMBER_TIMES - tempNumber;
            else finalThreadNumber = requestNumber / THREAD_NUMBER_TIMES;
        }
        return finalThreadNumber;
    }

    /**
     * 执行线程任务
     * @param requestNumber
     */
    public static ApiRunVo startPool(int requestNumber,String name,String type, Callable callable) throws Exception {
        initThreadPool(requestNumber);
        System.out.println(executors);
        futureList.clear();
        for (int i = 0 ; i<requestNumber;++i){
            Future future = executors.submit(callable);
            futureList.add(future);
        }
        ThreadPoolUtil.countDownLatch.await();
        return VoSettingUtil.setApiRunVo(futureList,name,type,requestNumber);
    }


    /**
     * 关闭线程池
     */
    public static void closeThreadPool(){
        ThreadPoolUtil.executors.shutdown();
        countDownLatch = null;
        futureList=null;
        ThreadPoolUtil.executors = null;
    }

}
