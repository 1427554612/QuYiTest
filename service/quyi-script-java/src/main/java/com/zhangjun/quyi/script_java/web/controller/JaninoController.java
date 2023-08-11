package com.zhangjun.quyi.script_java.web.controller;


import com.zhangjun.quyi.script_java.web.service.JaninoService;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/script")
@Api("测试脚本调用服务端资源")
public class JaninoController {

    @Autowired
    private JaninoService janinoService;

    @PostMapping("/run")
    public ResultModel run(@RequestBody Map<String,Object> script) throws Exception {
        Object result = janinoService.run(script);
        return ResultModel.ok().data("data",result);
    }
}
