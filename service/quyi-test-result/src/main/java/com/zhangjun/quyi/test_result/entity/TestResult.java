package com.zhangjun.quyi.test_result.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    @ApiModelProperty("最后一次执行时间")
    private Date last_run_date;

    @ApiModelProperty("最后一次执行耗时")
    private int last_run_time;
}
