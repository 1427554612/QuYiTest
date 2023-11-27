package com.zhangjun.quyi.currency_test.performance.main;

import com.zhangjun.quyi.currency_test.performance.testcase.impl.CurrencyCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.MessageCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.ProxyCase;
import org.python.antlr.ast.Str;

public class RunApi {

    public static void main(String[] args) throws Exception {
        String m1 = "https://mexlucky-api.pre-release.xyz";
        String p2 = "https://philucky-api.pre-release.xyz";
        String b1 = "https://b1-api.pre-release.xyz";
//        MessageCase messageCase = new MessageCase(1,"https://mexlucky-api.pre-release.xyz");
//        messageCase.sendMessage().close();

        ProxyCase proxyCase = new ProxyCase(10,b1,null);
        proxyCase.inviteUser().close();

//        CurrencyCase currencyCase = new CurrencyCase(1,p2);
//        currencyCase.currencyTest();

    }
}
