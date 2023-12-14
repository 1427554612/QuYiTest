package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.api.BaseApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;
import com.zhangjun.quyi.currency_test.utils.ParamsBuilder;

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
    public UserCase registerAndRecharge() throws Exception {

        BaseApi baseApi = new BaseApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"B3");
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        
        ThreadPoolUtil.start(this.requestNumber,()->{

            try {
                // 注册
                ApiResultEntity register1 = baseApi.registerApi("");
                // 登录
                ApiResultEntity login1= baseApi.loginApi(register1.getParamList());
                // 充值
                ApiResultEntity recharge1 = baseApi.rechargeApi(login1.getParamList());
                Thread.sleep(3000);
                // 补单
                adminBaseApi.repairOrderApi(recharge1.getParamList(),adminLoginResult.getParamList());

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
