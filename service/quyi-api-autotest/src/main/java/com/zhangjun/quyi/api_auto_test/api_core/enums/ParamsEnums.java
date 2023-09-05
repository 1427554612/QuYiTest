package com.zhangjun.quyi.api_auto_test.api_core.enums;

public interface ParamsEnums {

    /**
     * 参数来源枚举
     */
    enum ParamsFromEnum {

        REQUEST_HEADER("requestHeader"),
        REQUEST_BODY("requestBody"),
        RESPONSE_HEADER("responseHeader"),
        RESPONSE_BODY("responseBody");

        public String value;

        ParamsFromEnum(String value){
            this.value = value;
        }

    }

    /**
     * 参数类型枚举
     */
    enum ParamsSymbolEnum {

        API_PARAMS_SYMBOL("#"),       // 接口运行参数
        DIV_PARAMS_SYMBOL("%"),       // 自定义参数
        SYSTEM_PARAMS_SYMBOL("·");    // 系统参数


        public String symbol;
        ParamsSymbolEnum(String symbol){
            this.symbol = symbol;
        }
    }


    /**
     * 系统参数类别枚举
     */
    enum SystemParamsTypeEnum {

        TIME("time"),       // 接口运行参数
        THREAD_MAME("thread"),       // 自定义参数
        UUID("uuid");    // 系统参数


        public String type;
        SystemParamsTypeEnum(String type){
            this.type = type;
        }
    }
}
