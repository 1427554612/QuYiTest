package com.zhangjun.quyi.api_auto_test.entity.remoteEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode
@ToString
@ApiModel("结果详情")
@TableName(autoResultMap = true)
public class TestResultInfo {

    @ApiModelProperty("详情id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String resultInfoId;

    @ApiModelProperty("结果id")
    private String resultId;

    @ApiModelProperty("执行成功率")
    private boolean runResult;

    @ApiModelProperty("执行配置id")
    private String platformId;

    @ApiModelProperty("执行开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date runBeginTime;

    @ApiModelProperty("执行结束时间")
    private Date runEndTime;

    @ApiModelProperty("执行时间")
    private long runTime;


    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("数据")
    private Map<String,Object> datas;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("响应数据")
    private Map<String,Object> resultData;

    @ApiModelProperty("结果日志")
    private String resultLog;

}
