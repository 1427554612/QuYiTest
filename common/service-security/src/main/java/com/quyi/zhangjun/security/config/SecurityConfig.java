package com.quyi.zhangjun.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * SpringSecurity的默认配置并不能满足需求，所以我们需要自定义配置。
 * 配置SpringSecurity的方法也比较简单，继承WebSecurityConfigurerAdapter，
 * 一般我们会重写其中的3个configure方法。三个config分别有这不同的作用。
 *      1)、configure(AuthenticationManagerBuilder auth)：该方法的参数与认证有关：SecurityBuilder 用来创建一个AuthenticationManager。
 *          允许轻松构建内存身份验证、LDAP 身份验证、基于 JDBC 的身份验证、添加 UserDetailsService 和添加 AuthenticationProvider
 *
 *      2）、configure(HttpSecurity http):资源权限配置，该方法传入的参数为HttpSecurity ，主要有以下几个作用 （过滤器链）
 *          1、配置被拦截的资源
 *          2、拦截对应的角色权限（即拥有某个权限才可以访问）
 *          3、定义认证方式：httpBasic或者httpForm
 *          4、自定义登录界面、登录请求地址、错误处理方式等
 *          5、自定义SpringSecurity过滤器
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * SpringSecurity 5.x之后密码必须编码否则会报空指针
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 自定义身份认证
     * 该配置类的用于用户的认证
     *  1、提供身份信息（用户名、密码、角色、权限）
     *  2、配置认证信息的存储方式：内存、数据库
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //基于内存的存储方式
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("admin");
    }



    /**
     * 资源权限配置（过滤器链）
     * 1、配置被拦截的资源
     * 2、拦截对应的角色权限（即拥有某个权限才可以访问）
     * 3、定义认证方式：httpBasic或者httpForm
     * 4、自定义登录界面、登录请求地址、错误处理方式等
     * 5、自定义SpringSecurity过滤器
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //采用HttpBasic认证
        http.formLogin()
                //自定义登录界面
                .loginPage("/login/toLogin")
                //用户名的参数名
                .usernameParameter("username")
                //密码的参数名
                .passwordParameter("password")
                //登录请求的地址(默认是/login)
                .loginProcessingUrl("/login/doLogin")
                //登录成功后转发到(这里是post请求，因为是“转发”，不适重定向)
                // defaultSuccessUrl 不管怎么设置、都是跳转到index页面
                // .successForwardUrl()：不支持post请求方式
                .defaultSuccessUrl("/")
                //登录失败
                .failureForwardUrl("/login/toFail")
                .and()
                .authorizeRequests()
                //对于login请求直接放开
                .antMatchers("/login/toLogin").permitAll()
                //所有请求
                .anyRequest()
                //都需要认证
                .authenticated()
                .and().csrf().disable()
        ;
    }

    /**
     * configure(WebSecurity web)：用于影响全局安全性(配置资源，设置调试模式，通过实现自定义防火墙定义拒绝请求)的配置设置。一
     *      般用于配置全局的某些通用事物，例如静态资源等一、
     * @param web
     */
    @Override
    public void configure(WebSecurity web){

    }
}
