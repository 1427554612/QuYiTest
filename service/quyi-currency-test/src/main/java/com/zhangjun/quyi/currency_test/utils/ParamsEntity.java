package com.zhangjun.quyi.currency_test.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamsEntity implements Serializable {

    private String useName;
    private String from;
    private String keyName;
    private Object keyValue;

}
