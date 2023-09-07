package com.zhangjun.quyi.currency_test.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeCode {

    private String number;
    private String token;
    private String user_id;
}
