package com.zhangjun.quyi.user.controller;

import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 登录接口
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录")
    public ResultModel login(){
        return ResultModel.ok().data("token","asd56154egd8zw48df");
    }


    /**
     * 查询用户信息接口
     * @return
     */
    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public ResultModel info(){
        return ResultModel.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avater","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    @ApiOperation(value = "用户退出")
    public ResultModel logout(){
        return ResultModel.ok();
    }



}
