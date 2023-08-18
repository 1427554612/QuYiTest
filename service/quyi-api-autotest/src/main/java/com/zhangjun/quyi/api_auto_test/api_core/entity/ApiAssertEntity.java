package com.zhangjun.quyi.api_auto_test.api_core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiAssertEntity {
    String key;           // 断言匹配的key, 例：data._id
    Object expectValue;   // 预期的值
    Object realValue;     // 从实际key中拿到的值
    String from;          // 从哪里拿到key
    String type;          // 判断类型
}
