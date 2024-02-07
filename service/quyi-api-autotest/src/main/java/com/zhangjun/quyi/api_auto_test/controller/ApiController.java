package com.zhangjun.quyi.api_auto_test.controller;

import com.zhangjun.quyi.api_auto_test.entity.ApiEntity;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.ApiService;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/api-autotest/test-api")
@Api(description = "测试用例管理")
public class ApiController {

    @Autowired
    private ApiService apiService;

    /**
     * 获取所有用例列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取所有接口")
    public ResultModel selectAllApi() throws Exception {
        List<ApiEntity> apiList = apiService.list();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,apiList);
    }
}
