package com.zhangjun.quyi.currency_test.performance.testcase.impl;

import com.zhangjun.quyi.currency_test.performance.api.AdminBaseApi;
import com.zhangjun.quyi.currency_test.performance.testcase.BaseCase;

import java.io.IOException;

public class CurrencyCase extends BaseCase {
    /**
     * 基类初始化操作
     *
     * @param requestNumber
     * @param baseUrl
     */
    public CurrencyCase(Integer requestNumber, String baseUrl) {
        super(requestNumber, baseUrl, CurrencyCase.class);
    }


    public void currencyTest() throws IOException {
        AdminBaseApi adminBaseApi = new AdminBaseApi();
        adminBaseApi.artificialRecharge();
    }
}
