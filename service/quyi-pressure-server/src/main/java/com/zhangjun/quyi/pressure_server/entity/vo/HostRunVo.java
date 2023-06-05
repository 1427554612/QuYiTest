package com.zhangjun.quyi.pressure_server.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HostRunVo {
    private String address;
    private Integer requestNumber;
    private Integer allApiRequestNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date hostStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date hostEndTime;
    private long hostRunTime;
    private Integer poolThreadNumber;
    private List<ApiRunVo> apiRunVoList;
}
