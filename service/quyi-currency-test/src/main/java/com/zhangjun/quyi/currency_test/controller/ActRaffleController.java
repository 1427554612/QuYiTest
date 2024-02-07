package com.zhangjun.quyi.currency_test.controller;

import com.zhangjun.quyi.currency_test.core.testcase.impl.Act_RaffleApiCase;
import com.zhangjun.quyi.currency_test.core.testcase.impl.ProxyCase;
import com.zhangjun.quyi.currency_test.entity.BaseScriptEntity;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/script/actRaffle")
@Api(description = "转盘裂变活动")
public class ActRaffleController {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class ActRaffleEntity extends BaseScriptEntity {

        @ApiModelProperty("用户token")
        String token;

        @ApiModelProperty("用户id")
        String user_id;

        @ApiModelProperty("活动id")
        String biz_id;

        @ApiModelProperty("使用次数")
        Integer clickNumber;

        @ApiModelProperty("异步执行")
        boolean async;
    }

    @PostMapping("/draw")
    @ApiOperation("点击游玩")
    public ResultModel draw(@ApiParam(name = "ActRaffleEntity",value = "代理配置对象")
                                @RequestBody ActRaffleController.ActRaffleEntity actRaffleEntity ) throws  Exception{
        Act_RaffleApiCase act_raffleApiCase = new Act_RaffleApiCase(actRaffleEntity.getRequestNumber(),
                actRaffleEntity.getClientUrl(),
                actRaffleEntity.getAdminUrl());
        act_raffleApiCase.draw(actRaffleEntity.getUser_id(),actRaffleEntity.getToken(),actRaffleEntity.getClickNumber(),actRaffleEntity.isAsync());
        return ResultModel.ok().data("data",act_raffleApiCase.results);
    }

    /**
     * 提现奖金
     * @param actRaffleEntity
     * @return
     * @throws Exception
     */
    @PostMapping("/claim")
    @ApiOperation("转盘提现")
    public ResultModel claim(@ApiParam(name = "ActRaffleEntity",value = "代理配置对象")
                                     @RequestBody ActRaffleController.ActRaffleEntity actRaffleEntity ) throws Exception {
        Act_RaffleApiCase act_raffleApiCase = new Act_RaffleApiCase(actRaffleEntity.getRequestNumber(),
                actRaffleEntity.getClientUrl(),
                actRaffleEntity.getAdminUrl());
        act_raffleApiCase.claim(actRaffleEntity.getPlatform(),actRaffleEntity.getBiz_id(),actRaffleEntity.getUser_id(),actRaffleEntity.getToken());
        return ResultModel.ok().data("data",act_raffleApiCase.results);
    }

}
