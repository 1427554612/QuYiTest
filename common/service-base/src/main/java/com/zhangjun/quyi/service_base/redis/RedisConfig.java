package com.zhangjun.quyi.service_base.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setConnectionFactory(factory);
        //key序列化方式
        template.setKeySerializer(redisSerializer);
        //value序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);
        //value hashmap序列化
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    /**
     * 1分钟
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager1Minute(RedisConnectionFactory connectionFactory) {
        return baseManager(connectionFactory,60L);
    }

    /**
     * 3分钟
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager3Minute(RedisConnectionFactory connectionFactory) {
        return baseManager(connectionFactory,180L);
    }

    /**
     * 10分钟
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager10Minute(RedisConnectionFactory connectionFactory) {
        return baseManager(connectionFactory,600L);
    }

    /**
     * 一小时
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager1Hour(RedisConnectionFactory connectionFactory) {
        return baseManager(connectionFactory,3600L);
    }

    /**
     * 一天
     * @Primary：指定默认缓存器
     * @param connectionFactory
     * @return
     */
    @Bean
    @Primary
    public RedisCacheManager cacheManager1Day(RedisConnectionFactory connectionFactory) {
        return baseManager(connectionFactory,3600 * 24L);
    }

    /**
     * 基础缓存管理器
     * @param connectionFactory
     * @param ttlTime
     * @return
     */
    private RedisCacheManager baseManager(RedisConnectionFactory connectionFactory,long ttlTime){
        RedisCacheConfiguration config = instanceConfig(ttlTime);
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
    }


    /**
     * 序列化
     * @param ttl
     * @return
     */
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        // 去掉各种@JsonSerialize注解的解析
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        // 只针对非空的值进行序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 将类型序列化到属性json字符串中
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(ttl))
                .disableCachingNullValues() // 禁止缓存null值
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));
    }

}
