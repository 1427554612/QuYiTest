package com.zhangjun.quyi.pressure_server.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ApiRunVo {
    private String apiName;
    private String apiType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date apiStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date apiEndTime;

    private Integer requestNumber;
    private long apiRunTime;
    private long maxRunTime;
    private long minRunTime;
    private long avgRunTime;
    private double tps;
    private double errorRate;
    private long apiStartTimeStamp;
    private long apiEndTimeStamp;
    private List<ThreadRunVo> threadRunVoList = new ArrayList<>();
}
