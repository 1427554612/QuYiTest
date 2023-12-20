package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.core.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.core.utils.ResultWriterUtil;
import com.zhangjun.quyi.currency_test.core.utils.ThreadPoolUtil;

/**
 * 站内信相关用例
 */
public class MessageCase extends BaseCase {
    /**
     * 基类初始化操作
     *
     * @param requestNumber
     */
    public MessageCase(Integer requestNumber, String clientUrl, String adminUrl) {
        super(requestNumber,clientUrl,adminUrl,MessageCase.class);
        System.out.println("当前地址：" + this.adminUrl);
    }

    /**
     * 发送站内信
     */
    public MessageCase sendMessage() throws Exception {

        ResultWriterUtil.initFile("d:/result-"+ System.currentTimeMillis() + ".json",false);

        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"B1");

        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();

        ThreadPoolUtil threadPoolUtil = new ThreadPoolUtil();
        threadPoolUtil.start(this.requestNumber,()->{

            try {
                // 发送站内信
                adminBaseApi.sendMessageApi(adminLoginResult.getParamList());

                threadPoolUtil.countDownLatch.countDown();

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        System.out.println("任务执行结束...");
        threadPoolUtil.countDownLatch.await();
        this.close();
        return this;
    }
}
