package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.sun.org.apache.regexp.internal.RE;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.api.impl.GameApi;
import com.zhangjun.quyi.currency_test.performance.api.impl.TaskApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;
import com.zhangjun.quyi.currency_test.utils.ParamsBuilder;

public class GameCase extends BaseCase {

    /**
     * 基类初始化操作
     *
     * @param requestNumber
     * @param baseUrl
     * @param adminUrl
     */
    public GameCase(Integer requestNumber, String baseUrl, String adminUrl) {
        super(requestNumber, baseUrl, adminUrl, GameCase.class);
    }

    /**
     * 投注游戏
     * @return
     * @throws Exception
     */
    public GameCase betGame() throws Exception{

        GameApi gameApi = new GameApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"N1");
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity result = gameApi.registerApi("",false);
                // 登录
                ApiResultEntity loginApiResult= gameApi.loginApi(result.getParamList());
                // 充值
                ApiResultEntity rechargeApiResult = gameApi.rechargeApi(loginApiResult.getParamList(),1000,-1);
                Thread.sleep(2000);
                // 补单
                adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(),adminLoginResult.getParamList());

                for (int i = 0; i < 10000; i++) {
                    // 投注dice
                    gameApi.diceBet(loginApiResult.getParamList());
                    Thread.sleep(2000);
                }

                this.threadPoolUtil.countDownLatch.countDown();

            }catch (Exception e){
                e.printStackTrace();
            }
        });
        System.out.println("任务执行结束...");
        this.threadPoolUtil.countDownLatch.await();
        this.close();
        return this;
    }
}
