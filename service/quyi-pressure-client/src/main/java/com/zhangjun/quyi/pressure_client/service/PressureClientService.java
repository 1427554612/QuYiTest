package com.zhangjun.quyi.pressure_client.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.pressure_client.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_client.entity.vo.PressureCountVo;

public interface PressureClientService {

    PressureCountVo runPressureTest(RequestParamEntity requestParamEntity) throws Exception;
}
