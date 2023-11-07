package com.zhangjun.quyi.api_auto_test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhangjun.quyi.api_auto_test.api_core.enums.ParamsEnums;
import com.zhangjun.quyi.api_auto_test.entity.ApiParamsEntity;
import com.zhangjun.quyi.api_auto_test.service.ExportModuleService;
import com.zhangjun.quyi.api_auto_test.service.ParamService;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.python.core.AstList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/api-autotest/param")
@Api(description = "自定义参数管理")
@Validated
public class ParamController {

    @Autowired
    private ParamService paramService;

    /**
     * 添加自定义参数
     * @param apiParamsEntity
     * @return
     */
    @PostMapping("/save")
    @ApiOperation("添加自定义参数")
    public ResultModel saveParam(@ApiParam(name = "apiParamsEntity",value = "参数实例")
                                     @Validated @RequestBody ApiParamsEntity apiParamsEntity){
        ApiParamsEntity resultApiParam = paramService.saveParam(apiParamsEntity);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultApiParam);
    }

    /**
     * 根据id查询参数
     * @return
     */
    @GetMapping("/get/{paramId}")
    @ApiOperation("根据id查询自定义参数")
    public ResultModel getById(@ApiParam(name = "paramId",value = "参数id")
                                 @NotNull(message = "id不能为空")
                                   @PathVariable(name = "paramId") String paramId){
        ApiParamsEntity resultApiParam = paramService.findById(paramId);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultApiParam);
    }

    /**
     * 根据id修改参数
     * @return
     */
    @PutMapping("/put/{paramId}")
    @ApiOperation("根据id查询自定义参数")
    public ResultModel putById(@ApiParam(name = "paramId",value = "参数id")
                               @PathVariable(name = "paramId") String paramId,
                               @ApiParam(name = "apiParamsEntity",value = "参数实例")
                               @RequestBody ApiParamsEntity apiParamsEntity) throws JsonProcessingException, ParseException {
        apiParamsEntity.setParamId(paramId);
        ApiParamsEntity resultApiParam = paramService.putById(apiParamsEntity);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultApiParam);
    }


    /**
     * 获取参数类型
     * @return
     */
    @GetMapping("/type")
    @ApiOperation("获取参数类型")
    public ResultModel getParamType() {
        List<Map<String, Object>> collect = paramService.getParamType();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,collect);
    }


}
