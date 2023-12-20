package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.core.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.core.api.BaseApi;
import com.zhangjun.quyi.currency_test.core.api.clientImpl.ReferralCodeApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;

/**
 * 兑换码相关用例
 */
public class ReferralCodeCase extends BaseCase {

    /**
     * 初始化操作
     *
     * @param requestNumber
     */
    public ReferralCodeCase(Integer requestNumber, String clientUrl, String adminUrl) {
        super(requestNumber,clientUrl,adminUrl,ReferralCodeCase.class);
    }

    /**
     * 使用兑换码
     * @param platform
     * @return
     * @throws Exception
     */
    public ReferralCodeCase userReferralCode(String platform,String code) throws Exception {

        BaseApi baseApi = new BaseApi(this.clientUrl);
        ReferralCodeApi referralCodeApi = new ReferralCodeApi(this.clientUrl);

        this.threadPoolUtil.start(this.requestNumber,()->{

            try {
                // 注册
                ApiResultEntity register1 = baseApi.registerApi("",false);
                this.results.add(register1);
                // 登录
                ApiResultEntity login1= baseApi.loginApi(register1.getParamList());
                this.results.add(login1);

                ApiResultEntity useCodeResult = referralCodeApi.useReferralCodeApi(login1.getParamList(), code);

                this.results.add(useCodeResult);
                this.threadPoolUtil.countDownLatch.countDown();

            }catch (Exception e){
                this.threadPoolUtil.countDownLatch.countDown();
                e.printStackTrace();
                logger.error("当前线程脚本执行错误，" + Thread.currentThread().getName() + "，错误原因：" + e.getMessage());
            }
        });
        logger.info("脚本执行结束...");
        this.threadPoolUtil.countDownLatch.await();
        this.close();
        return this;
    }
}
