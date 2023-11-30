package com.zhangjun.quyi.currency_test.performance.main;

import com.zhangjun.quyi.currency_test.performance.testcase.impl.CurrencyCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.GameCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.MessageCase;
import com.zhangjun.quyi.currency_test.performance.testcase.impl.ProxyCase;
import org.python.antlr.ast.Str;

public class RunApi {

    public static void main(String[] args) throws Exception {
        String m1 = "https://mexlucky-api.pre-release.xyz";
        String p2_pre = "https://philucky-api.pre-release.xyz";
        String p2_dev = "https://philucky-api.pre-release.xyz";
        String b1 = "https://b1-api.pre-release.xyz";
        String n1 = "https://n1-api.pre-release.xyz";
        String admin_pre = "https://betgame-globalportal.pre-release.xyz";
        String admin_dev = "https://betgame-globalportal.dev.pre-release.xyz";
        MessageCase messageCase = new MessageCase(5,p2_dev,admin_dev);
        messageCase.sendMessage().close();

//        ProxyCase proxyCase = new ProxyCase(5,m1,admin);
//        proxyCase.inviteUser().close();

//        GameCase gameCase = new GameCase(5, n1, admin);
//        gameCase.betGame().close();


//        CurrencyCase currencyCase = new CurrencyCase(1,p2);
//        currencyCase.currencyTest();

    }
}
