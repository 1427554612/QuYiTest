package com.zhangjun.quyi.pressure_server.entity.apiEntity;

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
