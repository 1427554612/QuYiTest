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


    @GetMapping("/getCaseSuccessRate")
    @ApiOperation(value = "获取每个用例的执行成功率")
    public ResultModel getCaseSuccessRate() throws JsonProcessingException {
        Map<String, Object> data = echartsDataService.getCaseSuccessRate();
        return ResultModel.ok().data(data);
    }
}
