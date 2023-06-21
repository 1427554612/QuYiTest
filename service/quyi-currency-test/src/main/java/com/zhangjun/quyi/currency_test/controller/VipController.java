package com.zhangjun.quyi.currency_test.controller;

import com.zhangjun.quyi.currency_test.testcase.CurrencyTestCase;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/vip")
public class VipController {

    @GetMapping("/test/{requestNumber}")
    public ResultModel vipTest(@PathVariable Integer requestNumber) throws InterruptedException, IOException {
        CurrencyTestCase.run(requestNumber);
        return ResultModel.ok();
    }
}
