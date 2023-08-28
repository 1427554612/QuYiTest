package com.zhangjun.quyi.api_auto_test.api_core.log;

import com.zhangjun.quyi.utils.DateTimeUtil;
import org.slf4j.Logger;

import java.util.Date;


/**
 * 日志设置类
 */
public class LogStringBuilder {


    /**
     * 日志追加器
     */
    public static Logger logger;
    private static StringBuilder logBuilder = new StringBuilder();
    public static final String START_TEST = "开始执行测试任务...";
    public static final String CASE_START = "用例开始执行...";
    public static final String CASE_NAME = "用例名称：";
    public static final String SKIP_CASE = "用例被跳过...";
    public static final String PARAMS_HANDLE_RUN  = "参数处理器执行：";
    public static final String BEFORE_SCRIPT_RUN = "前置脚本执行结果：";
    public static final String AFTER_SCRIPT_RUN = "后置脚本执行结果";
    public static final String PARAMS_HANDLE_NUMBER = "参数处理器数量：";
    public static final String REPLACE_PARAMS_DATA = "替换后的数据：";
    public static final String RESPONSE_BODY_DATA = "响应体的数据：";
    public static final String SET_PARAMS_DATA = "设置的参数：";
    public static final String CASE_END = "执行结束....";

    /**
     * 添加日志
     * @param log
     * @return
     */
    public static void addLog(String log){
        logger.debug(log);
        LogStringBuilder.logBuilder
                .append("<span style = 'color:blue'>")
                .append(DateTimeUtil.dateForString(new Date()))
                .append("</span>")
                .append("  ")
                .append(log)
                .append("<br><br>");
    }

    /**
     * 获取日志
     * @return
     */
    public static String getLog(){
        return LogStringBuilder.logBuilder.toString();
    }

    /**
     * 设置日志长度
     * @return
     */
    public static void setLength(Integer size){
        LogStringBuilder.logBuilder.setLength(size);
    }


}
