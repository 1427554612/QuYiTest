package com.zhangjun.quyi.api_auto_test.entity.remoteEntity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TestConfigInfo对下", description="")
@TableName(autoResultMap = true)
public class TestConfigInfo {

    @ApiModelProperty(value = "配置详情表id")
    @TableId(value = "config_info_id", type = IdType.ID_WORKER_STR)
    private String configInfoId;

    @ApiModelProperty(value = "配置表id")
    private String configId;

    @ApiModelProperty(value = "配置数据")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String,Object> configData;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
