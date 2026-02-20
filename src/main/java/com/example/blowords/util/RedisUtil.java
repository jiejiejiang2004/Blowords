package com.example.blowords.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类（封装常用操作）
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存储键值对（带过期时间）
     * @param key 键
     * @param value 值
     * @param expireSeconds 过期时间（秒）
     */
    public void set(String key, Object value, long expireSeconds) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 获取值
     * @param key 键
     * @return 值（字符串类型）
     */
    public String get(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Object value = valueOperations.get(key);
        return value == null ? null : value.toString();
    }

    /**
     * 删除键
     * @param key 键
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 判断键是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
