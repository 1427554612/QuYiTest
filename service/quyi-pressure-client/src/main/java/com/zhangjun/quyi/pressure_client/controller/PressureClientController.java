package com.zhangjun.quyi.pressure_client.controller;

import com.zhangjun.quyi.pressure_client.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_client.entity.vo.PressureCountVo;
import com.zhangjun.quyi.pressure_client.service.PressureClientService;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pressure-client/v1")
public class PressureClientController {

    @Autowired
    private PressureClientService pressureClientService;

    @PostMapping("/runPressureTest")
    public ResultModel runPressureTest(@RequestBody @Validated RequestParamEntity requestParamEntity) throws Exception {
        PressureCountVo pressureCountVo = pressureClientService.runPressureTest(requestParamEntity);
        return ResultModel.ok().data("data",pressureCountVo);
    }
}
