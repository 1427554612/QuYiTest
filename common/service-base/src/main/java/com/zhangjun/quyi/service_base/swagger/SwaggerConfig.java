package com.zhangjun.quyi.service_base.swagger;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger配置类
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)

                .apiInfo(webApiInfo())
                .select()
//                .paths(Predicates.not(PathSelectors.regex("/admin/.*")))
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();

    }
    private ApiInfo webApiInfo(){
        return new ApiInfoBuilder()
                .title("张军-测试脚本-接口文档")
                .description("详细运行日志打开ie浏览器，输入地址：ftp://192.168.5.213:2121 ,账号密码：admin/123456")
                .version("1.0")
                .contact(new Contact("java", "http://zhangjunApi.com", "1427554612@qq.com"))
                .build();
    }
}
