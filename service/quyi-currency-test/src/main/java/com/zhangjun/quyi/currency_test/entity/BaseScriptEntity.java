package com.zhangjun.quyi.currency_test.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel
public class BaseScriptEntity {

    @ApiModelProperty("请求数量")
    Integer requestNumber;

    @ApiModelProperty("前台地址")
    String clientUrl;

    @ApiModelProperty("后台地址")
    String adminUrl;

    @ApiModelProperty("平台标识")
    String platform;
}
