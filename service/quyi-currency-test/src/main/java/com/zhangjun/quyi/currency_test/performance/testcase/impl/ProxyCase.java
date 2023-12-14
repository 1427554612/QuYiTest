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
    public ProxyCase(Integer requestNumber, String clientUrl, String adminUrl) {
        super(requestNumber,clientUrl,adminUrl,ProxyCase.class);
    }

    /**
     * 邀请下级用户获取人头费
     * @return
     */
    public ProxyCase inviteUser() throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"B1");
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        ApiResultEntity registerApiResult = taskApi.registerApi("");
        ThreadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity result = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult.getParamList(),"user_id"));
                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(result.getParamList());
                Thread.sleep(3000);
                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList());
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
     * 第三级人头充值
     * @return
     */
    public ProxyCase threeRecomendar() throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"K1");
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
                Thread.sleep(2000);
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
     * 全三级人头充值
     * @return
     */
    public ProxyCase allRecomendar() throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,"m1");
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        // 最上级注册
        ApiResultEntity register = taskApi.registerApi("");
        ThreadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity register1 = taskApi.registerApi((String) ParamsBuilder.getStr(register.getParamList(),"user_id"));
                // 登录
                ApiResultEntity login1= taskApi.loginApi(register1.getParamList());
                // 充值
                ApiResultEntity recharge1 = taskApi.rechargeApi(login1.getParamList());
                Thread.sleep(2000);
                // 补单
                adminBaseApi.repairOrderApi(recharge1.getParamList(),adminLoginResult.getParamList());

                // 注册
                ApiResultEntity register2 = taskApi.registerApi((String) ParamsBuilder.getStr(register1.getParamList(),"user_id"));
                // 登录
                ApiResultEntity login2= taskApi.loginApi(register2.getParamList());
                // 充值
                ApiResultEntity recharge2 = taskApi.rechargeApi(login2.getParamList());
                Thread.sleep(2000);
                // 补单
                adminBaseApi.repairOrderApi(recharge2.getParamList(),adminLoginResult.getParamList());

                // 注册
                ApiResultEntity register3 = taskApi.registerApi((String) ParamsBuilder.getStr(register2.getParamList(),"user_id"));
                // 登录
                ApiResultEntity login3= taskApi.loginApi(register3.getParamList());
                // 充值
                ApiResultEntity recharge3 = taskApi.rechargeApi(login3.getParamList());
                Thread.sleep(2000);
                // 补单
                adminBaseApi.repairOrderApi(recharge3.getParamList(),adminLoginResult.getParamList());
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
