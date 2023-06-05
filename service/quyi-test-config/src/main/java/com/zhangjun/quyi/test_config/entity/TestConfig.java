package com.zhangjun.quyi.test_config.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author testjava
 * @since 2023-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TestConfig对象", description="")
// 以resultMap方式返回
@TableName(autoResultMap = true)
public class TestConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置表id")
    @TableId(value = "config_id", type = IdType.ID_WORKER_STR)
    private String configId;

    @ApiModelProperty(value = "配置名称")
    @NotNull(message = "配置名称不能为空")
    @Length(min = 3,max = 20,message = "字符串长度在3到20之间")
    private String configName;

    @ApiModelProperty(value = "配置参数")
    // 将前端传递的json数据转化为map传递
    @TableField(typeHandler = JacksonTypeHandler.class)
    @NotNull(message = "配置参数不能为空")
    private Map<String,Object> configData;

    @ApiModelProperty(value = "配置类型:取值 web-ui,api,phone-ui,performance")
    @NotNull(message = "配置类型不能为空")
    private String configType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    private String updateUp;

    @ApiModelProperty(value = "备注")
    @NotNull(message = "配置备注不能为空")
    private String configMark;

}
