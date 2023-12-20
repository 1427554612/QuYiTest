package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.entity.ApiResultEntity;
import com.zhangjun.quyi.currency_test.core.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.core.api.clientImpl.GameApi;
import com.zhangjun.quyi.currency_test.core.api.clientImpl.TaskApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import com.zhangjun.quyi.currency_test.utils.ParamsBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

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
    public ProxyCase inviteOneUser(String platform ,Integer amount,Integer taskId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);
        ApiResultEntity registerApiResult = taskApi.registerApi("",isActivity);
        this.results.add(registerApiResult);
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity result = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult.getParamList(),"user_id"),isActivity);
                this.results.add(result);
                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(result.getParamList());
                this.results.add(loginApiResult);
                Thread.sleep(1000);
                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList(),amount,taskId);
                this.results.add(rechargeApiResult);
                // 补单
                ApiResultEntity repair = adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(), adminLoginResult.getParamList());
                this.results.add(repair);
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



    /**
     * 邀请下级用户获取人头费并投注
     * @return
     */
    public ProxyCase inviteOneUserBet(String platform ,Integer amount,Integer taskId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        GameApi gameApi = new GameApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        ApiResultEntity registerApiResult = taskApi.registerApi("",isActivity);
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity result = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult.getParamList(),"user_id"),isActivity);
                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(result.getParamList());
                Thread.sleep(3000);
                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList(),amount,taskId);
                // 补单
                adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(),adminLoginResult.getParamList());

                gameApi.diceBet(loginApiResult.getParamList());
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

    /**
     * 指定上级邀请下级并投注
     * @return
     */
    public ProxyCase appointParentInviteOneUserBet(String platform ,Integer amount,Integer taskId,String parentId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);

        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                ApiResultEntity result = taskApi.registerApi(parentId,isActivity);
                this.results.add(result);
                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(result.getParamList());
                this.results.add(loginApiResult);
                Thread.sleep(1000);
                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList(),amount,taskId);
                this.results.add(rechargeApiResult);
                // 补单
                ApiResultEntity repair = adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(), adminLoginResult.getParamList());
                this.results.add(repair);
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


    /**
     * 第三级人头充值
     * @return
     */
    public ProxyCase threeLevelUserRecharge(String platform ,Integer amount,Integer taskId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);
        // 后台三级投注
        ApiResultEntity registerApiResult1 = taskApi.registerApi("",isActivity);
        this.results.add(registerApiResult1);
        ApiResultEntity registerApiResult2 = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult1.getParamList(),"user_id"),isActivity);
        this.results.add(registerApiResult2);
        ApiResultEntity registerApiResult3 = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult2.getParamList(),"user_id"),isActivity);
        this.results.add(registerApiResult3);
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity registerApiResult = taskApi.registerApi((String) ParamsBuilder.getStr(registerApiResult3.getParamList(),"user_id"),isActivity);
                this.results.add(registerApiResult);

                // 登录
                ApiResultEntity loginApiResult= taskApi.loginApi(registerApiResult.getParamList());
                this.results.add(loginApiResult);
                // 充值
                ApiResultEntity rechargeApiResult = taskApi.rechargeApi(loginApiResult.getParamList(),amount,taskId);
                this.results.add(rechargeApiResult);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repairOrder = adminBaseApi.repairOrderApi(rechargeApiResult.getParamList(), adminLoginResult.getParamList());
                this.results.add(repairOrder);
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


    /**
     * 全三级人头充值
     * @return
     */
    public ProxyCase allThreeUserRecharge(String platform ,Integer amount,Integer taskId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);
        // 最上级注册
        ApiResultEntity register = taskApi.registerApi("",isActivity);
        this.results.add(register);
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity register1 = taskApi.registerApi((String) ParamsBuilder.getStr(register.getParamList(),"user_id"),isActivity);

                this.results.add(register1);
                // 登录
                ApiResultEntity login1= taskApi.loginApi(register1.getParamList());

                this.results.add(login1);
                // 充值
                ApiResultEntity recharge1 = taskApi.rechargeApi(login1.getParamList(),amount,taskId);
                this.results.add(recharge1);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair1 = adminBaseApi.repairOrderApi(recharge1.getParamList(), adminLoginResult.getParamList());
                this.results.add(repair1);

                // 注册
                ApiResultEntity register2 = taskApi.registerApi((String) ParamsBuilder.getStr(register1.getParamList(),"user_id"),isActivity);

                this.results.add(register2);
                // 登录
                ApiResultEntity login2= taskApi.loginApi(register2.getParamList());

                this.results.add(login2);
                // 充值
                ApiResultEntity recharge2 = taskApi.rechargeApi(login2.getParamList(),amount,taskId);
                this.results.add(recharge2);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair2 =  adminBaseApi.repairOrderApi(recharge2.getParamList(),adminLoginResult.getParamList());
                this.results.add(repair2);

                // 注册
                ApiResultEntity register3 = taskApi.registerApi((String) ParamsBuilder.getStr(register2.getParamList(),"user_id"),isActivity);
                this.results.add(register3);
                // 登录
                ApiResultEntity login3= taskApi.loginApi(register3.getParamList());
                this.results.add(login3);
                // 充值
                ApiResultEntity recharge3 = taskApi.rechargeApi(login3.getParamList(),amount,taskId);
                this.results.add(recharge3);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair3 =  adminBaseApi.repairOrderApi(recharge3.getParamList(),adminLoginResult.getParamList());
                this.results.add(repair3);

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

    /**
     * 全三级人头充值和投注
     * @return
     */
    public ProxyCase allThreeUserRechargeAndBet(String platform ,Integer amount,Integer taskId,boolean isActivity) throws Exception {
        TaskApi taskApi = new TaskApi(this.clientUrl);
        GameApi gameApi = new GameApi(this.clientUrl);
        AdminBaseApi adminBaseApi = new AdminBaseApi(this.adminUrl,platform);
        // 后台登录
        ApiResultEntity adminLoginResult = adminBaseApi.adminLoginApi();
        this.results.add(adminLoginResult);
        // 最上级注册
        ApiResultEntity register = taskApi.registerApi("",isActivity);
        this.results.add(register);
        this.threadPoolUtil.start(this.requestNumber,()->{
            try {
                // 注册
                ApiResultEntity register1 = taskApi.registerApi((String) ParamsBuilder.getStr(register.getParamList(),"user_id"),isActivity);

                this.results.add(register1);
                // 登录
                ApiResultEntity login1= taskApi.loginApi(register1.getParamList());

                this.results.add(login1);
                // 充值
                ApiResultEntity recharge1 = taskApi.rechargeApi(login1.getParamList(),amount,taskId);
                this.results.add(recharge1);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair1 = adminBaseApi.repairOrderApi(recharge1.getParamList(), adminLoginResult.getParamList());
                this.results.add(repair1);

                ApiResultEntity bet1 = gameApi.diceBet(login1.getParamList());
                this.results.add(bet1);

                // 注册
                ApiResultEntity register2 = taskApi.registerApi((String) ParamsBuilder.getStr(register1.getParamList(),"user_id"),isActivity);

                this.results.add(register2);
                // 登录
                ApiResultEntity login2= taskApi.loginApi(register2.getParamList());

                this.results.add(login2);
                // 充值
                ApiResultEntity recharge2 = taskApi.rechargeApi(login2.getParamList(),amount,taskId);
                this.results.add(recharge2);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair2 =  adminBaseApi.repairOrderApi(recharge2.getParamList(),adminLoginResult.getParamList());
                this.results.add(repair2);

                ApiResultEntity bet2 = gameApi.diceBet(login2.getParamList());
                this.results.add(bet2);

                // 注册
                ApiResultEntity register3 = taskApi.registerApi((String) ParamsBuilder.getStr(register2.getParamList(),"user_id"),isActivity);
                this.results.add(register3);
                // 登录
                ApiResultEntity login3= taskApi.loginApi(register3.getParamList());
                this.results.add(login3);
                // 充值
                ApiResultEntity recharge3 = taskApi.rechargeApi(login3.getParamList(),amount,taskId);
                this.results.add(recharge3);
                Thread.sleep(2000);
                // 补单
                ApiResultEntity repair3 =  adminBaseApi.repairOrderApi(recharge3.getParamList(),adminLoginResult.getParamList());
                this.results.add(repair3);

                ApiResultEntity bet3 = gameApi.diceBet(login3.getParamList());
                this.results.add(bet3);

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
