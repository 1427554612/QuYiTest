package com.zhangjun.quyi.currency_test.controller;


import com.sun.org.apache.regexp.internal.RE;
import com.zhangjun.quyi.currency_test.constant.PlatformStrConstant;
import com.zhangjun.quyi.currency_test.entity.BaseScriptEntity;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.UserCase;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.net.ftp.FtpClient;

import java.util.Map;

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
        try {
            userCase.registerAndRecharge(registerAndRechargeBean.getPlatform(),
                    registerAndRechargeBean.getAmount(),registerAndRechargeBean.getTaskId(),registerAndRechargeBean.isActivity()).close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            userCase.close();
        }
        return ResultModel.ok().data("data",userCase.results);
    }
}
