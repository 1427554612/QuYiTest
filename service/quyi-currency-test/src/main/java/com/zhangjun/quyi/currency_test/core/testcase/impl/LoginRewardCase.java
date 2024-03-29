package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.core.api.clientImpl.TaskApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.core.utils.ResultWriterUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * 首登奖励测试
 */
public class LoginRewardCase extends BaseCase {

    private RabbitTemplate rabbitTemplate;

    /**
     * 初始化操作
     *
     * @param requestNumber
     */
    public LoginRewardCase(Integer requestNumber,String clientUrl,String adminUrl,RabbitTemplate rabbitTemplate) {
        super(requestNumber,clientUrl,adminUrl,LoginRewardCase.class);
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 测试多用户转账
     * @return
     */
    public LoginRewardCase testReward() throws Exception {

        ResultWriterUtil.initFile("d:/result-"+ System.currentTimeMillis() + ".json",true);

        TaskApi taskApi = new TaskApi(this.clientUrl);

        // 注册
        ApiResultEntity registerApiResult = taskApi.registerApi("",false);
        rabbitTemplate.convertAndSend(registerApiResult);

        // 登录
        ApiResultEntity loginApiResult= taskApi.loginApi(registerApiResult.getParamList());
        rabbitTemplate.convertAndSend(loginApiResult);

        // 判断是否有奖励
        ApiResultEntity myRewardApiResult = taskApi.myRewardApi(loginApiResult.getParamList());
        rabbitTemplate.convertAndSend(myRewardApiResult);

        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 获取奖励
                ApiResultEntity useFirstLoginRewardApiEntity = taskApi.useFirstLoginRewardApi(
                        loginApiResult.getParamList(),
                        myRewardApiResult.getParamList());
                rabbitTemplate.convertAndSend(useFirstLoginRewardApiEntity);

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
