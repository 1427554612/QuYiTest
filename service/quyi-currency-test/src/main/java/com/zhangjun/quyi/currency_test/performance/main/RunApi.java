package com.zhangjun.quyi.currency_test.performance.main;

import com.zhangjun.quyi.currency_test.performance.testcase.impl.MessageCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.ProxyCase;

public class RunApi {

    public static void main(String[] args) throws Exception {
//        MessageCase messageCase = new MessageCase(1,"https://mexlucky-api.pre-release.xyz");
//        messageCase.sendMessage().close();

        ProxyCase proxyCase = new ProxyCase(10,"https://mexlucky-api.pre-release.xyz",null);
        proxyCase.inviteUser().close();
    }
}
