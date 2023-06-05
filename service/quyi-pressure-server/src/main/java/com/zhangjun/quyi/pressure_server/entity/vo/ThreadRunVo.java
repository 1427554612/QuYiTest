package com.zhangjun.quyi.pressure_server.entity.vo;

import lombok.Data;

import java.util.Map;

@Data
public class ThreadRunVo {
    	private String threadName;
        private String threadStartTime;
        private String threadEndTime;
        private long threadRunTime;
        private boolean error;
        private long threadStartTimeStamp;
        private long threadEndTimeStamp;
        private Map<String,String> params;
        private String errorMsg;
}
