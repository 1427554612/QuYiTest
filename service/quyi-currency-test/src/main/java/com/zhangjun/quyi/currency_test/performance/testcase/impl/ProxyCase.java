package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.api.impl.TaskApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.performance.utils.ResultWriterUtil;
import com.zhangjun.quyi.currency_test.performance.utils.ThreadPoolUtil;
import com.zhangjun.quyi.currency_test.utils.ParamsBuilder;
import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import com.zhangjun.quyi.utils.JsonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;
import java.util.List;

/**
 * 代理测试
 */
public class ProxyCase extends BaseCase {

    private RabbitTemplate rabbitTemplate;


    /**
     * 初始化操作
     *
     * @param requestNumber
     */
    public ProxyCase(Integer requestNumber,String baseUrl,RabbitTemplate rabbitTemplate) {
        super(requestNumber,baseUrl,ProxyCase.class);
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 邀请下级用户获取人头费
     * @return
     */
    public ProxyCase inviteUser() throws Exception {

        logger.info("rabbitTemplate  = " + rabbitTemplate);

        ResultWriterUtil.initFile("d:/result-"+ System.currentTimeMillis() + ".json",false);

        TaskApi taskApi = new TaskApi(this.baseUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi();

        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();

        ApiResultEntity registerApiResult = taskApi.registerApi("");

        ThreadPoolUtil.start(this.requestNumber,()->{

            try {
                // 注册
                ApiResultEntity result = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult.getParamList(),"user_id"));

                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(result.getParamList());

                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList());
                Thread.sleep(3000);
                // 补单
                adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(),adminLoginResult.getParamList());

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


    /**
     * 三级人头返利
     * @return
     */
    public ProxyCase threeRecomendar() throws Exception {
        logger.info("rabbitTemplate  = " + rabbitTemplate);

        ResultWriterUtil.initFile("d:/result-"+ System.currentTimeMillis() + ".json",false);

        TaskApi taskApi = new TaskApi(this.baseUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi();

        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();

        // 后台三级投注
        ApiResultEntity registerApiResult1 = taskApi.registerApi("");
        ApiResultEntity registerApiResult2 = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult1.getParamList(),"user_id"));
        ApiResultEntity registerApiResult3 = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult2.getParamList(),"user_id"));

        ThreadPoolUtil.start(this.requestNumber,()->{

            try {
                // 注册
                ApiResultEntity registerApiResult = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult3.getParamList(),"user_id"));

                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(registerApiResult.getParamList());

                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList());
                Thread.sleep(3000);
                // 补单
                adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(),adminLoginResult.getParamList());

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
