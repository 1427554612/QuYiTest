package com.zhangjun.quyi.test_statistics.controller;

import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;

@RestController
@RequestMapping("/api/test_statistics")
@Api(description = "结果数据统计")
public class ResultChartsController {

}
