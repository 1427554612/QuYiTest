package com.zhangjun.quyi.test_result.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName(autoResultMap=true)
@Data
@EqualsAndHashCode
@ToString
@ApiModel(value = "测试结果")
public class TestResult {

    @TableId(type = IdType.ID_WORKER_STR)
    @ApiModelProperty("结果id")
    private String result_id;

    @ApiModelProperty("用例名称")
    private String case_name;

    @ApiModelProperty("用例类型")
    private String case_type;

    @ApiModelProperty("执行次数")
    private int run_num;

    @ApiModelProperty("执行成功次数")
    private int run_success_num;

    @ApiModelProperty("执行失败次数")
    private int run_error_num;

    @ApiModelProperty("执行成功率")
    private double run_success_rate;

    @ApiModelProperty("最后一次执行结果")
    private String last_run_result;

    @ApiModelProperty("最后一次执行时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date last_run_date;

    @ApiModelProperty("最后一次执行耗时")
    private int last_run_time;
}
