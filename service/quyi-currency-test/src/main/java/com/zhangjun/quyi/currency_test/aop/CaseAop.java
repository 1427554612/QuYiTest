package com.zhangjun.quyi.currency_test.aop;

import com.zhangjun.quyi.currency_test.core.api.BaseApi;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

/**
 * aop类,目标类必须要存在于spring容器
 */
@Aspect
@Component
public class CaseAop {

    Logger logger = Logger.getLogger(CaseAop.class);

    @Pointcut("execution(* com.zhangjun.quyi.currency_test.core.api.*.*(..))")
    private  void getPointAnnotation(){}

    private  void startLog(){
        logger.info("前置通知：method is start...");
    }

    private  void endLog(){
        logger.info("后置通知：is run...");
    }
    private  void exceptionLog(){
        logger.info("异常通知：method execute is exception...");
    }

    private  void finalLog(){
        logger.info("最终通知：system exit...");
    }

    // 定义环绕通知
    @Around("getPointAnnotation()")
    public Object aroundLog(ProceedingJoinPoint pjp) throws Throwable {
        Object[] arg = pjp.getArgs();
        startLog();
        try {
            pjp.proceed(arg);
            endLog();
        }catch (Throwable e){
            exceptionLog();
            throw e;
        }finally {
            finalLog();
        }
        return true;
    }
}
