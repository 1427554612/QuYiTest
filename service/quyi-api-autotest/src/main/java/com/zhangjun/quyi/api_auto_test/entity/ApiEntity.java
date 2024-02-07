package com.zhangjun.quyi.api_auto_test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@ApiModel("模块实例对象")
@TableName(autoResultMap = true,value = "test_api")
public class ApiEntity {

    @ApiModelProperty(value = "接口id")
    @TableId(type = IdType.ID_WORKER_STR,value = "api_id")
    String apiId ;

    @ApiModelProperty("接口名称")
    String apiName;

    @ApiModelProperty("接口地址")
    String apiPath;

    @ApiModelProperty("接口类型")
    String apiType;

    @ApiModelProperty("请求方式")
    String requestMethod;

    @ApiModelProperty("是否是主流程")
    boolean isMainProcessApi;

    @ApiModelProperty("请求体")
    Object requestHeaders;

    @ApiModelProperty("请求体")
    Object requestBody;

    @ApiModelProperty("是否执行")
    boolean isRun;

    @ApiModelProperty("断言对象")
    Object assertMap;

    @ApiModelProperty("是否需要参数化")
    boolean isParams;

    @ApiModelProperty("参数列表")
    Object paramList;

    @ApiModelProperty("创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date updateTime;
}
