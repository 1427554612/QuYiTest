package com.zhangjun.quyi.currency_test.controller;


import com.zhangjun.quyi.currency_test.core.testcase.impl.ReferralCodeCase;
import com.zhangjun.quyi.currency_test.entity.BaseScriptEntity;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/script/referralCode")
@Api(description = "兑换码脚本")
public class ReferralCodeController {


    @ApiModel
    @Data
    @NoArgsConstructor
    static class ReferralCodeBean extends BaseScriptEntity {

        @ApiModelProperty("兑换码code")
        String number;

    }

    @PostMapping("/userReferralCode")
    @ApiOperation("使用兑换码")
    public ResultModel userReferralCode(@ApiParam(name = "configMap",value = "配置字段")
                                           @RequestBody ReferralCodeController.ReferralCodeBean referralCodeBean) throws Exception {
        ReferralCodeCase referralCodeCase = new ReferralCodeCase(referralCodeBean.getRequestNumber(),
                referralCodeBean.getClientUrl(),
                referralCodeBean.getAdminUrl());
        referralCodeCase.userReferralCode(referralCodeBean.getPlatform(),
                referralCodeBean.getNumber());
        return ResultModel.ok().data("data",referralCodeCase.results);
    }


}
