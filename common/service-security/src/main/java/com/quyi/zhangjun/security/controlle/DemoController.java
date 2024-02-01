package com.quyi.zhangjun.security.controlle;

import com.quyi.zhangjun.security.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 导入security 依赖后、浏览器打开 此服务端口、显示需要登录：
 *      默认账号密码是：user / 控制台Using generated security password 打印的字段
 *      xxxxProperties类：springboot启动器的默认配置类文件。如果想知道某个启动器可以设置哪些内容、可以去找相关properties配置类
 */

@RequestMapping(value = "/login")
@RestController
public class DemoController {

    @GetMapping(value = "/toLogin")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @PostMapping(value = "/toFail")
    public String fail() {
        System.out.println("调用失败");
        return "forward:/fail";
    }

    @GetMapping(value = "hello")
    public String hello() {
        return "hello";
    }
}
