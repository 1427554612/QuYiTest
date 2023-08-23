package com.zhangjun.quyi.test_result.entity;

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

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@TableName(autoResultMap=true)
@Data
@EqualsAndHashCode
@ToString
@ApiModel(value = "测试结果")
public class TestResult {

    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty("结果id")
    private String resultId;

    @ApiModelProperty("结果名称")
    private String caseName;

    @ApiModelProperty("结果类型")
    private String resultType;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("执行次数")
    private int runNum;

    @ApiModelProperty("执行成功次数")
    private int runSuccessNum;

    @ApiModelProperty("执行失败次数")
    private int runErrorNum;

    @ApiModelProperty("执行成功率")
    private double runSuccessRate;

    @ApiModelProperty("最后一次执行结果")
    private boolean lastRunResult;

    @ApiModelProperty("最后一次执行平台")
    private String lastRunPlatform;

    @ApiModelProperty("最后一次执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastRunDate;

    @ApiModelProperty("最后一次执行耗时")
    private long lastRunTime;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("数据")
    private Map<String,Object> datas;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("结果数据")
    private Map<String,Object> resultData;

    @ApiModelProperty("结果日志")
    private String resultLog;
}
