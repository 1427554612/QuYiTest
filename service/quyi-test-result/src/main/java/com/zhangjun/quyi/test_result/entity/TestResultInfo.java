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
    private String result_info_id;

    @ApiModelProperty("结果id")
    private String result_id;

    @ApiModelProperty("执行成功率")
    private boolean run_result;

    @ApiModelProperty("执行配置id")
    private String platform_id;

    @ApiModelProperty("执行开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date run_begin_time;

    @ApiModelProperty("执行结束时间")
    private Date run_end_time;

    @ApiModelProperty("执行成功率")
    private int run_time;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @ApiModelProperty("响应数据")
    private Map<String,Object> response_data;

}
