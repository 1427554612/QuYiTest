package com.zhangjun.quyi.currency_test.controller;


import com.zhangjun.quyi.currency_test.entity.BaseScriptEntity;
import com.zhangjun.quyi.currency_test.core.testcase.impl.UserCase;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/script/user")
@Api(description = "用户类脚本")
public class UserController {

    @ApiModel
    @Data
    @NoArgsConstructor
    static class UserScriptBean extends BaseScriptEntity{

        @ApiModelProperty("充值金额")
         Integer amount;

        @ApiModelProperty("任务id")
         Integer taskId;

        @ApiModelProperty("任务id")
        boolean activity;

    }

    @PostMapping("/registerAndRecharge")
    @ApiOperation("注册同时充值")
    public ResultModel registerAndRecharge(@ApiParam(name = "configMap",value = "配置字段")
                                            @RequestBody UserScriptBean registerAndRechargeBean) throws Exception {
        UserCase userCase = new UserCase(registerAndRechargeBean.getRequestNumber(),
                registerAndRechargeBean.getClientUrl(),
                registerAndRechargeBean.getAdminUrl());
        userCase.registerAndRecharge(registerAndRechargeBean.getPlatform(),
                registerAndRechargeBean.getAmount(),registerAndRechargeBean.getTaskId(),registerAndRechargeBean.isActivity());
        return ResultModel.ok().data("data",userCase.results);
    }


    @PostMapping("/multipleRecharge")
    @ApiOperation("用户多次充值")
    public ResultModel multipleRecharge(@ApiParam(name = "configMap",value = "配置字段")
                                            @RequestBody UserScriptBean registerAndRechargeBean) throws Exception {
        UserCase userCase = new UserCase(registerAndRechargeBean.getRequestNumber(),
                registerAndRechargeBean.getClientUrl(),
                registerAndRechargeBean.getAdminUrl());
        userCase.multipleRecharge(registerAndRechargeBean.getPlatform(),
                registerAndRechargeBean.getAmount(),registerAndRechargeBean.getTaskId(),registerAndRechargeBean.isActivity());
        return ResultModel.ok().data("data",userCase.results);
    }
}
