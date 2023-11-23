package com.zhangjun.quyi.currency_test.performance.testcase;

import com.zhangjun.quyi.currency_test.performance.testcase.impl.GPWalletCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.LoginRewardCase;
import com.zhangjun.quyi.currency_test.performance.utils.ResultWriterUtil;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;
import com.zhangjun.quyi.currency_test.testcase.CurrencyTestCase;
import com.zhangjun.quyi.utils.RequestUtil;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 基础case类
 */
public class BaseCase{


    public Logger logger;
    public String baseUrl;
    public Integer requestNumber;

    /**
     * 基类初始化操作
     * @param requestNumber
     * @param tClass
     */
    public BaseCase(Integer requestNumber, String baseUrl, Class<? extends BaseCase> tClass){
        this.baseUrl = baseUrl;
        this.requestNumber = requestNumber;
        this.logger = Logger.getLogger(tClass);
        RequestUtil.setOkhttpClient(this.requestNumber,this.requestNumber);
        ThreadPoolUtil.initThreadPool(this.requestNumber);
    }



    /**
     * 销毁操作
     * @throws IOException
     */
    public void close() throws Exception {
        ThreadPoolUtil.executors.shutdown();
        ThreadPoolUtil.countDownLatch = null;
        ResultWriterUtil.close();
    }

}
