package com.zhangjun.quyi.currency_test.core.testcase;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.core.utils.ResultWriterUtil;
import com.zhangjun.quyi.currency_test.core.utils.ThreadPoolUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 基础case类
 */
public class BaseCase{


    public Logger logger;
    public String clientUrl;
    public String adminUrl;
    public Integer requestNumber;
    private ExecutorService  executorService = null;
    public ThreadPoolUtil threadPoolUtil = null;
    public List<ApiResultEntity> results = Collections.synchronizedList(new ArrayList<>());   // 返回给接口的结果

    /**
     * 基类初始化操作
     * @param requestNumber
     * @param tClass
     */
    public BaseCase(Integer requestNumber, String baseUrl, String adminUrl,Class<? extends BaseCase> tClass){
        this.clientUrl = baseUrl;
        this.adminUrl = adminUrl;
        this.requestNumber = requestNumber;
        this.logger = Logger.getLogger(tClass);
        RequestUtil.setOkhttpClient(this.requestNumber,this.requestNumber);
        this.threadPoolUtil = new ThreadPoolUtil();
        threadPoolUtil.initThreadPool(this.requestNumber);
        this.executorService = threadPoolUtil.executors;
    }



    /**
     * 销毁操作
     * @throws IOException
     */
    public void close() throws Exception {
        this.executorService.shutdownNow();
        this.logger.info("线程池关闭状态：" + this.executorService.isShutdown());
        this.threadPoolUtil.countDownLatch = null;
        ResultWriterUtil.close();
    }

}
