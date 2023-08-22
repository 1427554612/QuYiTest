package com.zhangjun.quyi.api_auto_test.api_core.log;

/**
 * 日志设置类
 */
public class LogStringBuilder {

    /**
     * 日志追加器
     */
    private static StringBuilder logBuilder = new StringBuilder();
    public static final String START_TEST = "start test ...";
    public static final String CASE_NAME = "case_name:";
    public static final String SKIP_CASE = "case skip ...";
    public static final String PARAMS_HANDLE_RUN  = "params handle is run ...";
    public static final String BEFORE_SCRIPT_RUN = "before script run result:";
    public static final String AFTER_SCRIPT_RUN = "after script run result:";
    public static final String PARAMS_HANDLE_NUMBER = "params handle number:";
    public static final String REPLACE_PARAMS_DATA = "replace params data:";
    public static final String RESPONSE_BODY_DATA = "response body data:";
    public static final String SET_PARAMS_DATA = "set params data:";


    /**
     * 提供设置方法
     * @param log
     * @return
     */
    public static void addLog(String log){

        LogStringBuilder.logBuilder.append(log).append("\n");
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

    public static void main(String[] args) {
        LogStringBuilder.addLog("1111");
        LogStringBuilder.addLog("222");
        LogStringBuilder.addLog("333");
        String log = LogStringBuilder.getLog();
        System.out.println(log);
    }

}
