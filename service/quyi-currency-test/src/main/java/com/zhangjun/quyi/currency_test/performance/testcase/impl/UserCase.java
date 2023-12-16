package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.api.BaseApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;
import com.zhangjun.quyi.currency_test.utils.ParamsBuilder;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;

public class UserCase extends BaseCase {
    /**
     * 基类初始化操作
     *
     * @param requestNumber
     * @param baseUrl
     * @param adminUrl
     */
    public UserCase(Integer requestNumber, String baseUrl, String adminUrl) {
        super(requestNumber, baseUrl, adminUrl, UserCase.class);
    }


    /**
     * 注册与充值
     * @return
     */
    public UserCase registerAndRecharge(String platform,Integer amount,Integer taskId,boolean isActivity) throws Exception {

        BaseApi baseApi = new BaseApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);
        this.threadPoolUtil.start(this.requestNumber,()->{

            try {
                // 注册
                ApiResultEntity register1 = baseApi.registerApi("",isActivity);
                this.results.add(register1);
                // 登录
                ApiResultEntity login1= baseApi.loginApi(register1.getParamList());
                this.results.add(login1);
                // 充值
                ApiResultEntity recharge1 = baseApi.rechargeApi(login1.getParamList(),amount,taskId);
                this.results.add(recharge1);
                Thread.sleep(3000);
                // 补单
                ApiResultEntity repair = adminBaseApi.repairOrderApi(recharge1.getParamList(), adminLoginResult.getParamList());
                this.results.add(repair);
                this.threadPoolUtil.countDownLatch.countDown();

            }catch (Exception e){
                this.threadPoolUtil.countDownLatch.countDown();
                e.printStackTrace();
            }
        });
        this.threadPoolUtil.countDownLatch.await();
        this.close();
        return this;
    }
}
