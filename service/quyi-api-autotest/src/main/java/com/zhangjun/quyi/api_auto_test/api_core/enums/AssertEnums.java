package com.zhangjun.quyi.api_auto_test.api_core.enums;

/**
 * 断言组织枚举
 */
public interface AssertEnums {

    /**
     * 断言来源枚举
     */
    enum AssertFormEnum{
        RESPONSE_CODE("responseCode"),
        RESPONSE_HEADER("responseHeader"),
        RESPONSE_BODY("responseBody");

        public String value;

        AssertFormEnum(String value){
            this.value = value;
        }
    }

    /**
     * 断言类型枚举
     */
    enum AssertTypeEnum{
        EQUALS("=="),           // 等于
        NOT_EQUALS("!="),       // 不等于
        CONTAINS("<>"),         // 包含
        NOT_CONTAINS("!<>"),    // 不包含
        LIST_SIZE("size"),      // 判断数组长度
        IS_NULL("null"),        // 判断是空
        IS_NOT_NULL("!null");   // 判断不是空

      public String value;

      AssertTypeEnum(String value){
          this.value = value;
      }
    }
}
