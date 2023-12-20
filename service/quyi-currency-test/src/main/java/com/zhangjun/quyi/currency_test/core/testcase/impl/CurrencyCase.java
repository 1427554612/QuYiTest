package com.zhangjun.quyi.currency_test.core.testcase.impl;

import com.zhangjun.quyi.currency_test.core.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.core.testcase.BaseCase;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.IOException;

public class CurrencyCase extends BaseCase {
    /**
     * 基类初始化操作
     *
     * @param requestNumber
     */
    public CurrencyCase(Integer requestNumber, String clientUrl, String adminUrl, RabbitTemplate rabbitTemplate) {
        super(requestNumber,clientUrl,adminUrl,GPWalletCase.class);
    }


    public void currencyTest() throws IOException {
        AdminBaseApi adminBaseApi = new AdminBaseApi();
        adminBaseApi.artificialRecharge();
    }
}
