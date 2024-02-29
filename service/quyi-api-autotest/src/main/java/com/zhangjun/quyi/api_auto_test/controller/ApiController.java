package com.zhangjun.quyi.api_auto_test.controller;

import com.zhangjun.quyi.api_auto_test.entity.ApiEntity;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.ApiService;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 获取所有用例列表
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加接口")
    public ResultModel saveApi(@ApiParam(name = "apiEntity",value = "接口实例") @RequestBody ApiEntity apiEntity) throws Exception {
        boolean flag  = apiService.save(apiEntity);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,flag);
    }


    /**
     * 获取所有用例列表
     * @return
     */
    @PutMapping("/update")
    @ApiOperation(value = "修改接口")
    public ResultModel updateApi(@ApiParam(name = "",value = "")String apiId,
                                 @ApiParam(name = "apiEntity",value = "接口实例") @RequestBody ApiEntity apiEntity) throws Exception {
        apiEntity.setApiId(apiId);
        boolean flag  = apiService.updateById(apiEntity);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,flag);
    }


    /**
     * 获取所有用例列表
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation(value = "删除接口")
    public ResultModel updateApi(@ApiParam(name = "",value = "")String apiId) throws Exception {
        boolean flag  = apiService.removeById(apiId);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,flag);
    }
}
