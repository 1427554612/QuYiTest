package com.zhangjun.quyi.pressure_server.service.impl;

import com.zhangjun.quyi.constans.PressureConstant;
import com.zhangjun.quyi.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_server.entity.vo.ApiRunVo;
import com.zhangjun.quyi.pressure_server.entity.vo.HostRunVo;
import com.zhangjun.quyi.pressure_server.entity.vo.ThreadRunVo;
import com.zhangjun.quyi.pressure_server.service.PressureServerService;
import com.zhangjun.quyi.pressure_server.service.api.PressureAdminApi;
import com.zhangjun.quyi.pressure_server.service.api.PressureApi;
import com.zhangjun.quyi.pressure_server.utlis.*;
import com.zhangjun.quyi.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class PressureServerServiceImpl implements PressureServerService {

    private List<ApiRunVo> apiRunVoList = null;
    private volatile AtomicInteger i = null;
    private Logger logger = LoggerFactory.getLogger(PressureServerServiceImpl.class);


    /**
     * 执行压力测试
     * @param requestParamEntity
     * @throws Exception
     */
    @Override
    public HostRunVo runPressureTest(RequestParamEntity requestParamEntity) throws Exception {
        apiRunVoList = new ArrayList<>();
        i = new AtomicInteger(0);
        int requestNumber = requestParamEntity.getRequestNumber();
        RequestUtil.setOkhttpClient(requestNumber, requestNumber);


        ApiRunVo apiRunVo = ThreadPoolUtil.startPool(requestNumber,"添加埋点",PressureConstant.REQUEST_TYPE_POST, ()->{
            ThreadRunVo threadRunVo = PressureApi.reportApi(requestParamEntity);
            ThreadPoolUtil.countDownLatch.countDown();
            return threadRunVo;
        });
        apiRunVoList.add(apiRunVo);
        System.out.println(" reportApi  is  run  ok !!");
//        // 注册
//        ApiRunVo apiRunVo = ThreadPoolUtil.startPool(requestNumber,"注册",PressureConstant.REQUEST_TYPE_POST, ()->{
//            ThreadRunVo threadRunVo = PressureApi.registerApi(requestParamEntity);
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(apiRunVo);
//
//        // 登录
//        List<Map<String,String>> registerParams = ParamsUtil.getParams(apiRunVoList, "注册", "account", "password","grecaptcha_token");
//        ApiRunVo loginApiRunVo = ThreadPoolUtil.startPool(requestNumber,"登录",PressureConstant.REQUEST_TYPE_POST, ()->{
//            // 获取前置参数
//            ThreadRunVo threadRunVo = PressureApi.loginApi(requestParamEntity,registerParams.get(i.getAndIncrement()));
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(loginApiRunVo);

//        // 使用兑换码
//        List<Map<String,String>> loginParams = ParamsUtil.getParams(apiRunVoList, "登录", "_id", "token");
//        i = new AtomicInteger(0);
//        ApiRunVo userCodeApiRunVo = ThreadPoolUtil.startPool(requestNumber,"使用兑换码",PressureConstant.REQUEST_TYPE_POST, ()->{
//            // 获取前置参数
//            ThreadRunVo threadRunVo = PressureApi.userCodeApi(requestParamEntity,loginParams.get(i.getAndIncrement()));
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(userCodeApiRunVo);
//        System.out.println(" userCode  is  run  ok !!");

//        // 充值
//        List<Map<String,String>> loginParams = ParamsUtil.getParams(apiRunVoList, "登录", "_id", "token");
//        i = new AtomicInteger(0);
//        ApiRunVo rechargeApiRunVo = ThreadPoolUtil.startPool(requestNumber,"充值",PressureConstant.REQUEST_TYPE_POST, ()->{
//            // 获取前置参数
//            ThreadRunVo threadRunVo = PressureApi.rechargeApi(requestParamEntity,loginParams.get(i.getAndIncrement()));
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(rechargeApiRunVo);





//        // 投注
//        i = new AtomicInteger(0);
//        ApiRunVo betApiRunVo = ThreadPoolUtil.startPool(requestNumber,"投注",PressureConstant.REQUEST_TYPE_POST, ()->{
//            // 获取前置参数
//            ThreadRunVo threadRunVo = PressureApi.betApi(requestParamEntity,loginParams.get(i.getAndIncrement()));
//            ThreadPoolUtil.countDownLatch.countDown();
//            return threadRunVo;
//        });
//        apiRunVoList.add(betApiRunVo);
//        System.out.println(" rechargeApi  is  run  ok !!");

        HostRunVo hostRunVo = VoSettingUtil.setHostVo(apiRunVoList);
        ThreadPoolUtil.closeThreadPool();   // 初始化相关容器
        HostRunResultWriterUtil.writeResult(PressureConstant.WRITER_PATH,hostRunVo);
        return hostRunVo;
    }


    /**
     * 测试代理
     * @param requestParamEntity
     * @return
     */
    @Override
    public HostRunVo testProxy(RequestParamEntity requestParamEntity) throws Exception {
        apiRunVoList = new ArrayList<>();
        i = new AtomicInteger(0);
        int requestNumber = requestParamEntity.getRequestNumber();
        RequestUtil.setOkhttpClient(requestNumber, requestNumber);

        // 前台
        runApi(requestParamEntity,"注册",PressureConstant.REQUEST_TYPE_POST,apiRunVoList);
//        runApi(requestParamEntity,"登录",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "注册", "account", "password","grecaptcha_token"));
//        runApi(requestParamEntity,"充值",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "登录", "_id", "token"));
//
//        // 后台
//        runApi(requestParamEntity,"后台-查询",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "登录", "_id"));
//        runApi(requestParamEntity,"后台-补单",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "后台-查询", "trans_id"));

        HostRunVo hostRunVo = VoSettingUtil.setHostVo(apiRunVoList);
        ThreadPoolUtil.closeThreadPool();   // 初始化相关容器
        HostRunResultWriterUtil.writeResult(PressureConstant.WRITER_PATH,hostRunVo);
        return hostRunVo;
    }

    @Override
    public HostRunVo testUserCode(RequestParamEntity requestParamEntity) throws Exception {
        apiRunVoList = new ArrayList<>();
        i = new AtomicInteger(0);
        int requestNumber = requestParamEntity.getRequestNumber();
        RequestUtil.setOkhttpClient(requestNumber, requestNumber);

        // 前台
        runApi(requestParamEntity,"注册",PressureConstant.REQUEST_TYPE_POST,apiRunVoList);
        runApi(requestParamEntity,"登录",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "注册", "account", "password","grecaptcha_token"));

        // 使用兑换码
        runApi(requestParamEntity,"使用兑换码",PressureConstant.REQUEST_TYPE_POST,apiRunVoList,ParamsUtil.getParams(apiRunVoList, "登录", "_id", "token"));

        HostRunVo hostRunVo = VoSettingUtil.setHostVo(apiRunVoList);
        ThreadPoolUtil.closeThreadPool();   // 初始化相关容器
        HostRunResultWriterUtil.writeResult(PressureConstant.WRITER_PATH,hostRunVo);
        return hostRunVo;
    }


    /**
     * 执行api,不需要前置参数
     * @param requestParamEntity:controller实例对象
     * @param apiName：接口名称
     * @param requestType：请求类型
     * @param apiRunVoList：接口运行结果列表
     * @return
     * @throws Exception
     */
    private  ApiRunVo runApi(RequestParamEntity requestParamEntity, String apiName, String requestType,List<ApiRunVo> apiRunVoList) throws Exception {
        ApiRunVo apiRunVo = null;
        if (apiName.contains("注册")){
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(),apiName,requestType, ()->{
                ThreadRunVo threadRunVo = PressureApi.registerApi(requestParamEntity);
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
        }
        logger.info("registerApi run ok...");
        return apiRunVo;
    }


    /**
     * 执行api,需要前置参数
     * @param requestParamEntity:controller实例对象
     * @param apiName：接口名称
     * @param requestType：请求类型
     * @param apiRunVoList：接口运行结果列表
     * @param paramsList：参数列表
     * @return
     * @throws Exception
     */
    private ApiRunVo runApi(RequestParamEntity requestParamEntity, String apiName, String requestType, List<ApiRunVo> apiRunVoList, List<Map<String, String>> paramsList) throws Exception {
        ApiRunVo apiRunVo = null;
        if (apiName.equals("登录")) {
            i = new AtomicInteger(0);
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
                ThreadRunVo threadRunVo = PressureApi.loginApi(requestParamEntity, paramsList.get(i.getAndIncrement()));
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
            logger.info(" loginApi  is  run  ok !!");
        } else if (apiName.equals("充值")) {
            i = new AtomicInteger(0);
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
                ThreadRunVo threadRunVo = PressureApi.rechargeApi(requestParamEntity, paramsList.get(i.getAndIncrement()));
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
            logger.info(" rechargeApi  is  run  ok !!");
        }
        else if (apiName.equals("后台-查询")){
            i = new AtomicInteger(0);
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
                ThreadRunVo threadRunVo = PressureAdminApi.listRechargeApi(requestParamEntity, paramsList.get(i.getAndIncrement()));
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
            logger.info(" listRechargeApi  is  run  ok !!");
        }
        else if (apiName.equals("后台-补单")){
            i = new AtomicInteger(0);
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
                ThreadRunVo threadRunVo = PressureAdminApi.notifyRecharge(requestParamEntity, paramsList.get(i.getAndIncrement()));
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
            logger.info(" notifyRecharge  is  run  ok !!");

        }else if (apiName.equals("使用兑换码")){
            i = new AtomicInteger(0);
            apiRunVo = ThreadPoolUtil.startPool(requestParamEntity.getRequestNumber(), apiName, requestType, () -> {
                ThreadRunVo threadRunVo = PressureApi.userCodeApi(requestParamEntity, paramsList.get(i.getAndIncrement()));
                ThreadPoolUtil.countDownLatch.countDown();
                return threadRunVo;
            });
            apiRunVoList.add(apiRunVo);
            logger.info(" userCodeApi  is  run  ok !!");
        }
        return apiRunVo;
    }




    public static void main(String[] args) throws Exception {
        RequestParamEntity requestParamEntity = new RequestParamEntity();
        requestParamEntity.setRequestUrl(PressureConstant.BASE_CLIENT_URL);
        requestParamEntity.setRequestNumber(800);// 线程用户数
        requestParamEntity.setIps(Arrays.asList("192.168.5.12:8057"));
        new PressureServerServiceImpl().testUserCode(requestParamEntity);

    }

}
