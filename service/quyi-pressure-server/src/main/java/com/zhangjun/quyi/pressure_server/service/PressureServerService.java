package com.zhangjun.quyi.pressure_server.service;

import com.zhangjun.quyi.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_server.entity.vo.HostRunVo;

public interface PressureServerService {

    /**
     * 进行压力测试
     * @param requestParamEntity
     * @return
     * @throws Exception
     */
    HostRunVo runPressureTest(RequestParamEntity requestParamEntity) throws Exception;


    /**
     * 测试代理
     * @param requestParamEntity
     * @return
     */
    HostRunVo testProxy(RequestParamEntity requestParamEntity) throws Exception;

    /**
     * 测试使用兑换码
     * @param requestParamEntity
     * @return
     */
    HostRunVo testUserCode(RequestParamEntity requestParamEntity) throws Exception;


    /**
     * 测试站内信
     * @param requestParamEntity
     * @return
     * @throws Exception
     */
    HostRunVo testSendMessage(RequestParamEntity requestParamEntity) throws Exception;
}
