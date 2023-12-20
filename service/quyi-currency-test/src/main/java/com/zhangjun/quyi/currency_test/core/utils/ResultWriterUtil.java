package com.zhangjun.quyi.currency_test.core.utils;

import com.zhangjun.quyi.utils.JsonUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ResultWriterUtil {

    static BufferedWriter bufferedWriter = null;

    /**
     * 初始化
     * @param resultFilePath
     * @param append
     * @throws IOException
     */
    public static void initFile(String resultFilePath,boolean append) throws IOException {
        ResultWriterUtil.bufferedWriter = new BufferedWriter(new FileWriter(resultFilePath,append));
    }

    /**
     * 写入数据
     * @param text
     * @throws Exception
     */
    public static void writeResult(Object text) throws Exception {
        ResultWriterUtil.bufferedWriter.write(JsonUtil.objectMapper.writeValueAsString(text));
        ResultWriterUtil.bufferedWriter.flush();
    }

    public static void close() throws Exception {
        if (bufferedWriter!=null){
            ResultWriterUtil.bufferedWriter.close();
        }
    }
}
