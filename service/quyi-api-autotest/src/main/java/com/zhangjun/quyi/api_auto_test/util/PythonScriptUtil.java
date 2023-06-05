package com.zhangjun.quyi.api_auto_test.util;

import com.zhangjun.quyi.constans.EncodeConstant;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * python解释器
 */
@Slf4j
public class PythonScriptUtil {

    /**
     * 执行方法
     * @return
     */
    public static boolean execute(String cmd,String filePath) throws IOException {
        StringBuilder runCmd = new StringBuilder(StrConstant.CMD_CD)
                .append(StrConstant.SPACE_STR)
                .append(filePath)
                .append(" & ")
                .append(cmd);
        log.info("最终执行的命令是：" + runCmd);
        Process exec = Runtime.getRuntime().exec("cmd /c " + runCmd);
        InputStream inputStream = exec.getInputStream();
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(EncodeConstant.ENCODE_GBK) ));
        String text = null;
        while ((text = bf.readLine())!=null){
            // 当执行错误,停止任务执行
            if (text.contains("FAILURES")) {
                WebSocketServer.sendInfo("程序执行报错... 结束监控...", HttpConstant.CONNECTION_SID);
                break;
            }
            WebSocketServer.sendInfo(text,HttpConstant.CONNECTION_SID);
        }
        inputStream.close();
        bf.close();
        return true;
    }
}
