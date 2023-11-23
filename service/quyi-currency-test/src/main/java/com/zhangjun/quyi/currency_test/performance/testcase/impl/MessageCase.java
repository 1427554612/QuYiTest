package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.api.impl.TaskApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.performance.utils.ResultWriterUtil;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;

import java.io.IOException;

/**
 * 站内信相关用例
 */
public class MessageCase extends BaseCase {
    /**
     * 基类初始化操作
     *
     * @param requestNumber
     * @param baseUrl
     */
    public MessageCase(Integer requestNumber, String baseUrl) {
        super(requestNumber, baseUrl, MessageCase.class);
    }

    /**
     * 发送站内信
     */
    public MessageCase sendMessage() throws Exception {

        ResultWriterUtil.initFile("d:/result-"+ System.currentTimeMillis() + ".json",false);

        AdminBaseApi adminBaseApi = new AdminBaseApi();

        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();


        ThreadPoolUtil.start(this.requestNumber,()->{

            try {
                // 发送站内信
                adminBaseApi.sendMessageApi(adminLoginResult.getParamList());

                ThreadPoolUtil.countDownLatch.countDown();

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        System.out.println("任务执行结束...");
        ThreadPoolUtil.countDownLatch.await();
        this.close();
        return this;
    }
}
