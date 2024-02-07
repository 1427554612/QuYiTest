package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.core.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.core.api.BaseApi;
import com.zhangjun.quyi.currency_test.core.api.clientImpl.Act_RaffleApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;

public class Act_RaffleApiCase extends BaseCase {

    public Act_RaffleApiCase(Integer requestNumber, String baseUrl, String adminUrl) {
        super(requestNumber, baseUrl, adminUrl, Act_RaffleApiCase.class);
    }


    /**
     * 点击转盘
     * @param user_id
     * @param token
     * @param async
     * @return
     * @throws Exception
     */
    public Act_RaffleApiCase draw(String user_id,String token,Integer clickNumber,boolean async) throws Exception {
        Act_RaffleApi act_raffleApi = new Act_RaffleApi(this.clientUrl);
        if (async){
            this.threadPoolUtil.start(this.requestNumber,()->{

                try {
                    act_raffleApi.draw(user_id,token);
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
        }else {
            for (Integer i = 0; i < clickNumber; i++) {
                act_raffleApi.draw(user_id,token);
            }
            logger.info("脚本执行结束...");
        }
        return this;
    }

    /**
     * 提款
     * @return
     */
    public Act_RaffleApiCase claim(String platform,String biz_id,String user_id,String token) throws Exception {

        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);

        Act_RaffleApi act_raffleApi = new Act_RaffleApi(this.clientUrl);
        this.threadPoolUtil.start(this.requestNumber,()->{

            try {
                act_raffleApi.claim(biz_id,user_id,token);
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
