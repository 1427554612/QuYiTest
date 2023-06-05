package com.zhangjun.quyi.test_task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TaskCase对象", description="")
public class TaskCase implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "taskId")
    private String taskId;

    @ApiModelProperty(value = "caseId")
    private String caseId;


}
