package com.zhangjun.quyi.pressure_server.entity.apiEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String account;
    String password;
    String grecaptcha_token;
    String person;

    public User(String account,
            String password,
            String grecaptcha_token){
        this.account = account;
        this.password = password;
        this.grecaptcha_token = grecaptcha_token;
    }
}
