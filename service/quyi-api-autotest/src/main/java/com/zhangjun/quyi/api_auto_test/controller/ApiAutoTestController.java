package com.zhangjun.quyi.api_auto_test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhangjun.quyi.api_auto_test.api.TestConfigApi;
import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.ApiAutoTestService;
import com.zhangjun.quyi.constans.EncodeConstant;
import com.zhangjun.quyi.constans.HttpConstant;
import com.zhangjun.quyi.constans.StrConstant;
import com.zhangjun.quyi.service_base.websocket.WebSocketServer;
import com.zhangjun.quyi.utils.HtmlParseUtil;
import com.zhangjun.quyi.utils.JsonUtil;
import com.zhangjun.quyi.utils.ResultModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/api-autotest")
@Api(description = "测试用例管理")
public class ApiAutoTestController {

    @Autowired
    private ApiAutoTestService apiAutoTestService;

    @Autowired
    private TestConfigApi testConfigApi;

    @Autowired
    private RedisTemplate redisTemplate;




    /**
     * 执行指定名称的用例
     * @return
     */
    @GetMapping("/selectAllCase")
    @ApiOperation(value = "获取所有用例")
    @Cacheable(value = "TestCase",key = "'list'",cacheManager = "cacheManager10Second")
    public ResultModel selectAllCase() throws Exception {
        List<ApiTestCaseEntity> caseList = apiAutoTestService.selectAllCase();
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_LIST,caseList);
    }


    /**
     * 批量执行用例
     * @param caseList
     * @return
     */
    @PostMapping("/runCase/{configId}")
    @ApiOperation(value = "批量执行测试用例")
    public ResultModel runCase(@ApiParam(name = "caseList",value = "测试用例名称列表")
                                @RequestBody ArrayList<String> caseList,
                               @ApiParam(name = "configId",value = "全局配置id")
                               @PathVariable String configId) throws Exception {
        apiAutoTestService.runCase(caseList,configId);
        return ResultModel.ok();
    }

    /**
     * 执行指定名称的用例
     * @param caseName
     * @return
     */
    @GetMapping("/runCase/{caseName}/{configId}")
    @ApiOperation(value = "执行指定id的测试用例")
    public ResultModel runCase(@ApiParam(name = "caseName",value = "测试用例名称")
                               @PathVariable String caseName,
                               @ApiParam(name = "configId",value = "配置id")
                               @PathVariable String configId) throws IOException {
        apiAutoTestService.runCase(caseName,configId);
        return ResultModel.ok();
    }

    /**
     * 上传文件到本地
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation(value = "上传接口用例文件")
    public ResultModel upload(MultipartHttpServletRequest request) throws IOException {
        redisTemplate.delete("TestCase::list");
        Map<String,Object> resultMap = apiAutoTestService.upload(request);
        return ResultModel.ok().data(HttpConstant.RESPONSE_STR_DATA,resultMap);
    }


    /**
     * 下载接口
     * @return
     * @throws IOException
     */
    @GetMapping("/download/{fileName}")
    @ApiOperation(value = "下载接口")
    public ResponseEntity<InputStreamResource> download(@ApiParam(name = "fileName",value = "标识，随意填写、不填写则下载报告")@PathVariable(required = false) String fileName) throws IOException {
        Map<String, Object> configMap = testConfigApi.getConfigPath().getData();
        FileSystemResource file = null;
        System.out.println("fileName = " + fileName);
        if ("report".equals(fileName)){
            String reportPath = (String) configMap.get(StrConstant.REPORT_PATH);
            File htmlFile = HtmlParseUtil.updateHtmlFile(reportPath);
            file = new FileSystemResource(htmlFile.getPath());
        }else {
            JsonNode configNode = JsonUtil.objectMapper.readTree(new File((String) configMap.get(StrConstant.CONFIG_PATH)));
            file = new FileSystemResource(new File(configNode.get(StrConstant.EXCEL_PATH).asText()));
        }
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpConstant.REQUEST_HEADER_CONTENT_DISPOSITION,"attachment;filename="+new String(file.getFilename().getBytes(EncodeConstant.ENCODE_UTF_8), EncodeConstant.ENCODE_ISO_8859));
        headers.add(StrConstant.FILE_NAME,new String(file.getFilename().getBytes(EncodeConstant.ENCODE_UTF_8), EncodeConstant.ENCODE_ISO_8859));
        headers.set("Access-Control-Expose-Headers",StrConstant.FILE_NAME);
        headers.set(HttpConstant.REQUEST_HEADER_CONTENT_LENGTH,String.valueOf(file.getFile().length()));
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(HttpConstant.MIME_TYPE_HTML))
                .body(new InputStreamResource(file.getInputStream()));

    }

    //推送数据接口
    @ResponseBody
    @PostMapping("/socket/push/{sid}")
    @ApiOperation(value = "测试socket通信")
    public Map pushToWeb(@ApiParam(name = "sid",value = "连接id")@PathVariable String sid,
                         @ApiParam(name = "message",value = "发送的消息") @RequestBody String message) {
        Map<String,Object> result = new HashMap<>();
        try {
            WebSocketServer.sendInfo(message, sid);
            result.put("code", sid);
            result.put("msg", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }




}
