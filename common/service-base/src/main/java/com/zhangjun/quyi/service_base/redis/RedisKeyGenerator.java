package com.zhangjun.quyi.service_base.redis;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 自定义cache key生成器
 */
@Component(value = "redisKeyGenerator")
public class RedisKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
