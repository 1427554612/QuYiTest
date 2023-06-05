package com.zhangjun.quyi.pressure_server.controller;

import com.zhangjun.quyi.entity.RequestParamEntity;
import com.zhangjun.quyi.pressure_server.entity.vo.HostRunVo;
import com.zhangjun.quyi.pressure_server.service.PressureServerService;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pressure-server/v1")
public class PressureServerController {

    @Autowired
    private PressureServerService pressureServerService;

    @PostMapping("/runPressureTest")
    public ResultModel runPressureTest(@RequestBody @Validated RequestParamEntity requestParamEntity) throws Exception {
        HostRunVo hostRunVo = pressureServerService.runPressureTest(requestParamEntity);
        return ResultModel.ok().data("hostRunVo",hostRunVo);
    }
}
