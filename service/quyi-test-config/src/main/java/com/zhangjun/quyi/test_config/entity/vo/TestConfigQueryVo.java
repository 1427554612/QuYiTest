package com.zhangjun.quyi.test_config.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import java.util.Date;

/**
 * 查询vo类
 */
@Data
public class TestConfigQueryVo {

    @ApiModelProperty(value = "配置id")
    private String configId;

    @ApiModelProperty(value = "配置名称")
    @Length(min = 0,max = 20,message = "名称最大长度为20")
    private String configName;

    @ApiModelProperty(value = "配置类型:取值 web-ui,api,phone-ui,performance")
    private String configType;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty(value = "修改人")
    private String updateUp;
}
