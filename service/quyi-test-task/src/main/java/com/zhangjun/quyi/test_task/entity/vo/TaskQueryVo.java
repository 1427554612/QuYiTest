package com.zhangjun.quyi.test_task.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class TaskQueryVo {

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "执行类型")
    private String type;

    @ApiModelProperty(value = "开始时间")
    private String startLastBeginTime;

    @ApiModelProperty(value = "结束时间")
    private String endLastBeginTime;
}
