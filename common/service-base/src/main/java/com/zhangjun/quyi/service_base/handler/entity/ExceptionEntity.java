package com.zhangjun.quyi.service_base.handler.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类
 */
@Data                // 生产setting and getting方法
@AllArgsConstructor  //生成有参数构造方法
@NoArgsConstructor   //生成无参数构造
public class ExceptionEntity extends RuntimeException {
    private Integer code;
    private String message;
}
