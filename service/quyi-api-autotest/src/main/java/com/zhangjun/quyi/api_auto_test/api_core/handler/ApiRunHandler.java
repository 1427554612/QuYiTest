package com.zhangjun.quyi.api_auto_test.api_core.handler;

import com.zhangjun.quyi.api_auto_test.entity.ApiTestCaseEntity;
import com.zhangjun.quyi.api_auto_test.service.impl.ApiAutoTestServiceImpl;
import com.zhangjun.quyi.api_auto_test.util.JavaJaninoUtil;
import com.zhangjun.quyi.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 核心控制器，控制自动化程序执行
 */
public class ApiRunHandler {

    // 默认请求数量
    public static final Integer DEFAULT_REQUEST_NUMBER = 10;
    private static Logger logger  = LoggerFactory.getLogger(ApiRunHandler.class);


    static {
        RequestUtil.setOkhttpClient(ApiRunHandler.DEFAULT_REQUEST_NUMBER,ApiRunHandler.DEFAULT_REQUEST_NUMBER);
    }

    /**
     * 执行方法
     * @param caseList：勾选的执行用例列表
     * @param configId：关联配置id
     */
    public static void runApi(List<ApiTestCaseEntity> caseList,String configId){
        caseList.stream().forEach(testCaseEntity -> {
            if (testCaseEntity.getBeforeScript()!=null&& !testCaseEntity.getBeforeScript().equals("")){
                try {
                    // 执行前置脚本
                    Object o = JavaJaninoUtil.runScript(testCaseEntity.getBeforeScript());
                    System.out.println(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (testCaseEntity.getAfterScript()!=null&& !testCaseEntity.getAfterScript().equals("")){
                try {
                    // 执行前置脚本
                    Object o = JavaJaninoUtil.runScript(testCaseEntity.getAfterScript());
                    System.out.println(o);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
