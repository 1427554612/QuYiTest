package com.zhangjun.quyi.pressure_client.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.pressure_client.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_client.entity.vo.PressureCountVo;
import com.zhangjun.quyi.pressure_client.service.PressureClientService;
import com.zhangjun.quyi.pressure_client.util.VoSettingUtil;
import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class PressureClientServiceImpl implements PressureClientService {

    private List<String> hostRunVos = null;
    private CountDownLatch countDownLatch = null;

    /**
     * 发送请求给远程服务器
     * @param requestParamEntity
     */
    @Override
    public PressureCountVo runPressureTest(RequestParamEntity requestParamEntity) throws InterruptedException, JsonProcessingException {
        hostRunVos = new ArrayList<>();
        countDownLatch = new CountDownLatch(requestParamEntity.getIps().size());
        List<String> ips = requestParamEntity.getIps();
        int requestNumber = requestParamEntity.getRequestNumber();
        int tempRequestNumber = requestNumber;
        RequestUtil.client = RequestUtil.setOkhttpClient(ips.size(),1);
        int temp = requestNumber % ips.size();
        requestParamEntity.setRequestNumber(requestNumber / ips.size());   // 前面的负载机设置任务数
        ips.stream().forEach( ip ->{
            if(ip.equals(ips.get(ips.size()-1))) requestParamEntity.setRequestNumber(requestNumber / ips.size() + temp);  // 最后的负载机设置任务数
            new Thread(()->{
                String responseBody = null;
                String message = null;
                String hostRunVo = null;
                try {
                    responseBody = RequestUtil.sendPost("http://" + ip+"/pressure-server/v1/runPressureTest", JsonUtil.objectMapper.writeValueAsString(requestParamEntity));
                    message = JsonUtil.objectMapper.readTree(responseBody).get("message").textValue();
                    hostRunVo = JsonUtil.objectMapper.readTree(responseBody).get("data").toString();
                    if (!message.equals("成功")) throw new ExceptionEntity(20001,"负载机请求错误，结果未正常返回");
                } catch (Exception e) {
                }

                hostRunVos.add(hostRunVo);
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
        requestParamEntity.setRequestNumber(tempRequestNumber);
        return VoSettingUtil.settingPressureCountVo(hostRunVos, requestParamEntity);
    }

//    public static void main(String[] args) throws Exception {
//        PressureClientServiceImpl pressureClientService = new PressureClientServiceImpl();
//        RequestParamEntity requestParamEntity = new RequestParamEntity();
//        requestParamEntity.setRequestUrl("https://aajogo-api.office.coinmoney.xyz");
//        requestParamEntity.setRequestNumber(10);
//        requestParamEntity.setIps(Arrays.asList("192.168.5.213:8057","192.168.4.215:8057"));
//        PressureCountVo pressureCountVo = pressureClientService.runPressureTest(requestParamEntity);
//        System.out.println(JsonUtil.objectMapper.writeValueAsString(pressureCountVo));
//    }
}
