package com.zhangjun.quyi.test_result.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.test_result.service.EchartsDataService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/test_result/charts")
@Api(description = "数据统计")
public class EchartsDataController {

    @Autowired
    private EchartsDataService echartsDataService;

    /**
     * 获取每个用例的执行成功率
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getCaseSuccessRate")
    @ApiOperation(value = "获取每个用例的执行成功率")
    public ResultModel getCaseSuccessRate() throws JsonProcessingException {
        return ResultModel.ok().data(echartsDataService.getCaseSuccessRate());
    }

    /**
     * 获取每个平台成功和失败总数
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getPlatformSuccessAndErrorNum")
    @ApiOperation(value = "获取每个用例的执行成功率")
    public ResultModel getPlatformSuccessAndErrorNum() throws JsonProcessingException {
        return ResultModel.ok().data(echartsDataService.getPlatformSuccessAndErrorNum());
    }

    /**
     * 获取当前成功和失败的用例
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/getCurrentSuccessAndErrorNum")
    @ApiOperation(value = "获取最近一次执行的接口成功和失败的比例")
    public ResultModel getCurrentSuccessAndErrorNum() throws JsonProcessingException {
        return ResultModel.ok().data(echartsDataService.getCurrentSuccessAndErrorNum());
    }
}
