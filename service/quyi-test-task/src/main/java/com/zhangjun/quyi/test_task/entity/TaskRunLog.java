package com.zhangjun.quyi.test_task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TaskRunLog对象", description="")
public class TaskRunLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "执行id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "开始执行时间")
    private Date beginTime;

    @ApiModelProperty(value = "执行结束时间")
    private Date endTime;

    @ApiModelProperty(value = "正确执行的用例条数")
    private Integer successCount;

    @ApiModelProperty(value = "执行失败的用例总数")
    private Integer errorCount;

    @ApiModelProperty(value = "任务id")
    private String taskId;

    @ApiModelProperty(value = "运行状态")
    private Integer runStatus;


}
