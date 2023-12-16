package com.zhangjun.quyi.currency_test.entity;


import com.zhangjun.quyi.currency_test.utils.ParamsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResultEntity implements Serializable {
    String name;
    String url;
    long startTime;
    long endTime;
    Map<String,Object> requestBody;
    Map<String,Object> responseBody;
    boolean result;
    List<ParamsEntity> paramList = new ArrayList<>();
}
