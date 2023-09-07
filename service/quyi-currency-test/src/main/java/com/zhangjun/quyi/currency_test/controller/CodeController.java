package com.zhangjun.quyi.currency_test.controller;


import com.zhangjun.quyi.currency_test.testcase.CurrencyTestCase;
import com.zhangjun.quyi.utils.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
public class CodeController {


    @PostMapping("/use")
    public ResultModel use(@RequestBody Map<String,Object> map) throws IOException, InterruptedException {
        CurrencyTestCase.run(map);
        return ResultModel.ok();
    }

    @GetMapping("/test")
    public ResultModel use() {

        return ResultModel.ok().data("message","成功");
    }

}
