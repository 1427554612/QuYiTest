package com.zhangjun.quyi.pressure_server.utlis;

import com.zhangjun.quyi.pressure_server.entity.vo.HostRunVo;
import com.zhangjun.quyi.utils.JsonUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class HostRunResultWriterUtil {


    public static void writeResult(String resultFileName, Object o) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFileName));
        bufferedWriter.write(JsonUtil.objectMapper.writeValueAsString(o));
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
