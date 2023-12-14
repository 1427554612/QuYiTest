package com.zhangjun.quyi.currency_test.performance.main;

import com.zhangjun.quyi.currency_test.performance.testcase.impl.*;
import org.python.antlr.ast.Str;

public class RunApi {

    public static void main(String[] args) throws Exception {
        String m1 = "https://mexlucky-api.pre-release.xyz";
        String p2_pre = "https://philucky-api.pre-release.xyz";
        String p2_dev = "https://philucky-api.pre-release.xyz";
        String b1_pre = "https://b1-api.pre-release.xyz";
        String b1_dev = "https://b1-api.dev.pre-release.xyz";
        String n1_pre = "https://n1-api.pre-release.xyz";
        String k1_pro = "https://api.kelucky.com";
        String b3_dev = "https://b3-api.dev.pre-release.xyz";
        String b3_pre = "https://b3-api.pre-release.xyz";
        String admin_pre = "https://betgame-globalportal.pre-release.xyz";
        String admin_dev = "https://betgame-globalportal.dev.pre-release.xyz";
        UserCase userCase = new UserCase(1,b3_dev,admin_dev);
        userCase.registerAndRecharge().close();

//        MessageCase messageCase = new MessageCase(100, b1_dev, admin_dev);
//        messageCase.sendMessage().close();

//        ProxyCase proxyCase = new ProxyCase(1,k1_pro,admin_pre);
//        proxyCase.allRecomendar().close();

//        GameCase gameCase = new GameCase(5, n1, admin);
//        gameCase.betGame().close();


//        CurrencyCase currencyCase = new CurrencyCase(1,p2);
//        currencyCase.currencyTest();

    }
}
