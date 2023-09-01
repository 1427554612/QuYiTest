package com.zhangjun.quyi.api_auto_test.controller;

import com.zhangjun.quyi.api_auto_test.entity.ModuleEntity;
import com.zhangjun.quyi.api_auto_test.service.ExportModuleService;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/api-autotest/module")
@Api(description = "内部模块管理")
public class ExportModuleController {

    @Autowired
    private ExportModuleService exportModuleService;

    /**
     * 导出所有模块
     * @return
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出所有模块")
    public ResultModel export(){
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,exportModuleService.export());
    }

    /**
     * 添加导出模块
     * @param moduleEntity
     * @return
     */
    @PostMapping("/save")
    @ApiOperation(value = "添加模块")
    public ResultModel saveEntity(@ApiParam(name ="moduleEntity",value = "内部模块实例") @RequestBody ModuleEntity moduleEntity){
        ModuleEntity resultModuleEntity = exportModuleService.saveEntity(moduleEntity);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultModuleEntity);
    }
}
