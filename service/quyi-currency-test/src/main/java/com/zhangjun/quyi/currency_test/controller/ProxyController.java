package com.zhangjun.quyi.currency_test.controller;

import com.zhangjun.quyi.currency_test.core.testcase.impl.ProxyCase;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/script/proxy")
@Api(description = "代理类脚本")
public class ProxyController {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class ProxyScriptBean extends UserController.UserScriptBean {

         @ApiModelProperty("上级id")
         String parentId;

    }

    /**
     * 邀请一级下级人数并充值
     * @param proxyScriptBean
     * @return
     * @throws Exception
     */
    @PostMapping("/inviteOneUser")
    @ApiOperation("邀请一级下级并充值")
    public ResultModel inviteOneUser(@ApiParam(name = "proxyScriptBean",value = "代理配置对象")
                                         @RequestBody ProxyController.ProxyScriptBean proxyScriptBean ) throws Exception {
        ProxyCase proxyCase = new ProxyCase(proxyScriptBean.getRequestNumber(),
                proxyScriptBean.getClientUrl(),
                proxyScriptBean.getAdminUrl());
        proxyCase.inviteOneUser(proxyScriptBean.getPlatform(),proxyScriptBean.getAmount(),proxyScriptBean.getParentId(),proxyScriptBean.getTaskId(), proxyScriptBean.isActivity());
        return ResultModel.ok().data("data",proxyCase.results);
    }

    /**
     * 邀请一级下级投注
     * @param proxyScriptBean
     * @return
     */
    @PostMapping("/inviteOneUserBet")
    @ApiOperation("邀请一级下级充值并投注")
    public ResultModel inviteOneUserBet(@ApiParam(name = "proxyScriptBean",value = "代理配置对象")
                                                     @RequestBody ProxyController.ProxyScriptBean proxyScriptBean) throws Exception {
        ProxyCase proxyCase = new ProxyCase(proxyScriptBean.getRequestNumber(),
                proxyScriptBean.getClientUrl(),
                proxyScriptBean.getAdminUrl());
        proxyCase.inviteOneUserBet(proxyScriptBean.getPlatform(),
                proxyScriptBean.getAmount(),
                proxyScriptBean.getTaskId(),
                proxyScriptBean.getParentId(),proxyScriptBean.isActivity());
        return ResultModel.ok().data("data",proxyCase.results);
    }

    /**
     * 邀请第三级下级用户充值
     * @param proxyScriptBean
     * @return
     * @throws Exception
     */
    @PostMapping("/threeLevelUserRecharge")
    @ApiOperation("邀请第三级子用户充值")
    public ResultModel threeLevelUserRecharge(@ApiParam(name = "proxyScriptBean",value = "代理配置对象")
                                     @RequestBody ProxyController.ProxyScriptBean proxyScriptBean ) throws Exception {
        ProxyCase proxyCase = new ProxyCase(proxyScriptBean.getRequestNumber(),
                proxyScriptBean.getClientUrl(),
                proxyScriptBean.getAdminUrl());
        proxyCase.threeLevelUserRecharge(proxyScriptBean.getPlatform(),proxyScriptBean.getAmount(),proxyScriptBean.getParentId(),proxyScriptBean.getTaskId(),proxyScriptBean.isActivity());
        return ResultModel.ok().data("data",proxyCase.results);
    }

    /**
     * 邀请第三级下级用户充值
     * @param proxyScriptBean
     * @return
     * @throws Exception
     */
    @PostMapping("/allThreeUserRecharge")
    @ApiOperation("依次邀请三级子用户充值")
    public ResultModel allThreeUserRecharge(@ApiParam(name = "proxyScriptBean",value = "代理配置对象")
                                              @RequestBody ProxyController.ProxyScriptBean proxyScriptBean ) throws Exception {
        ProxyCase proxyCase = new ProxyCase(proxyScriptBean.getRequestNumber(),
                proxyScriptBean.getClientUrl(),
                proxyScriptBean.getAdminUrl());
        proxyCase.allThreeUserRecharge(proxyScriptBean.getPlatform(),proxyScriptBean.getAmount(),proxyScriptBean.getTaskId(),proxyScriptBean.isActivity());
        return ResultModel.ok().data("data",proxyCase.results);
    }

    /**
     * 邀请第三级下级用户充值并投注
     * @param proxyScriptBean
     * @return
     * @throws Exception
     */
    @PostMapping("/allThreeUserRechargeAndBet")
    @ApiOperation("依次邀请三级子用户充值并投注")
    public ResultModel allThreeUserRechargeAndBet(@ApiParam(name = "proxyScriptBean",value = "代理配置对象")
                                            @RequestBody ProxyController.ProxyScriptBean proxyScriptBean ) throws Exception {
        ProxyCase proxyCase = new ProxyCase(proxyScriptBean.getRequestNumber(),
                proxyScriptBean.getClientUrl(),
                proxyScriptBean.getAdminUrl());
        proxyCase.allThreeUserRechargeAndBet(proxyScriptBean.getPlatform(),proxyScriptBean.getAmount(),proxyScriptBean.getTaskId(),proxyScriptBean.isActivity());
        return ResultModel.ok().data("data",proxyCase.results);
    }
}
