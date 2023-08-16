package com.zhangjun.quyi.api_auto_test.api_core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 参数设置实体类
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiParamsEntity {
    String caseName;   // 用例名称，表示这个参数创建于哪个用例
    String paramName;  // 参数名称，表示其他用例要如何引用此用例
    String paramFrom;  // 参数来源，表示参数从哪里来，requestHeader,requestBody,responseCode,responseHeader,responseBody
    String paramsEq;   // 匹配表达式，@xxx@表示内置，$xxx$表示自定义
    String paramValue; // 匹配到的值、设置到这个熟悉中
}
