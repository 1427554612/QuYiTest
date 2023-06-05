package com.zhangjun.quyi.service_base.handler;

import com.zhangjun.quyi.service_base.handler.entity.ExceptionEntity;
import com.zhangjun.quyi.utils.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理器
 * 如果是全局和特殊异常处理器、那么我们不用捕获、系统会自动帮我们抛出异常
 * 如果是自定义异常处理器，我们则需要捕获后手动抛出
 * 框架使用的是集中的异常处理，把各个Controller中抛出的异常集中到一个地方处理。处理异常的叫作异常处理器。
 *
 * springmvc框架中使用两个注解完成异常的集中处理，这样一来，每个Controller就不用单独处理异常了。
 *
 * @ExceptionHandler：放在方法的上面，表示此方法可以处理某个类型的异常，当异常发生时，执行这个方法。
 * @ControllerAdvice：放在类的上面，表示这个类中有异常的处理方法，这个注解可以看作是对控制器类的一个增强，就是给Controller类增加了异常处理的功能，相当于aop中的@Aspect。
 * 使用注解 @ExceptionHandler 可以将一个方法指定为异常处理方法。该注解只有一个可选属性 value，为一个 Class<?>数组，用于指定该注解的方法所要处理的异常类，即所要匹配的异常。
 * 而被注解的方法，其返回值可以是 ModelAndView、String，或 void，方法名随意，方法参数可以是 Exception 及其子类对象、HttpServletRequest、HttpServletResponse 等。系统会自动为这些方法参数赋值。
 */
@RestControllerAdvice
@Slf4j    // 自己写错误日志
public class GlobalExceptionHandler extends Throwable {

    /**
     * 全局异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultModel error(Exception e){
        e.printStackTrace();
        log.error(e.getMessage(),e);
        return ResultModel.error().message(e.getMessage());
    }

    /**
     * 特殊异常处理器
     */
    @ExceptionHandler(NullPointerException.class)
    public ResultModel error(NullPointerException e){
        log.error(e.getMessage(),e);
        return ResultModel.error().message(e.getMessage());
    }

    /**
     * 自定义异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(ExceptionEntity.class)
    public ResultModel error(ExceptionEntity e){
        String message = e.getMessage();
        // 将错误信息写入到异常日志中
        log.error(message,e);
        return ResultModel.error().code(e.getCode()).message(message);
    }

}
