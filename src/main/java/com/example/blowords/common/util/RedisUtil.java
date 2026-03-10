package com.example.blowords.common.util;

import com.example.blowords.common.model.RedisMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.Collections;
import java.util.List;
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

    public boolean verifyRegisterCaptcha(String email, String captcha) {
        String storedCaptcha = get("email:captcha:" + email);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    // ==================== 消息队列相关方法 ====================

/**
 * 入队（从队列左端插入）
 * @param queueName 队列名称
 * @param message 消息内容
 * @return 入队后队列长度
 */
public Long lpush(String queueName, Object message) {
    return redisTemplate.opsForList().leftPush(queueName, message);
}

/**
 * 入队（使用消息对象）
 * @param queueName 队列名称
 * @param message 消息对象
 * @return 入队后队列长度
 */
public Long lpush(String queueName, RedisMessage message) {
    return redisTemplate.opsForList().leftPush(queueName, message);
}

/**
 * 入队（从队列右端插入）
 * @param queueName 队列名称
 * @param message 消息内容
 * @return 入队后队列长度
 */
public Long rpush(String queueName, Object message) {
    return redisTemplate.opsForList().rightPush(queueName, message);
}

/**
 * 入队（使用消息对象）
 * @param queueName 队列名称
 * @param message 消息对象
 * @return 入队后队列长度
 */
public Long rpush(String queueName, RedisMessage message) {
    return redisTemplate.opsForList().rightPush(queueName, message);
}

/**
 * 出队（从队列左端取出）
 * @param queueName 队列名称
 * @return 消息内容
 */
public Object lpop(String queueName) {
    return redisTemplate.opsForList().leftPop(queueName);
}

/**
 * 出队（从队列左端取出，转换为消息对象）
 * @param queueName 队列名称
 * @return 消息对象
 */
public RedisMessage lpopMessage(String queueName) {
    Object result = redisTemplate.opsForList().leftPop(queueName);
    return result instanceof RedisMessage ? (RedisMessage) result : null;
}

/**
 * 出队（从队列右端取出）
 * @param queueName 队列名称
 * @return 消息内容
 */
public Object rpop(String queueName) {
    return redisTemplate.opsForList().rightPop(queueName);
}

/**
 * 出队（从队列右端取出，转换为消息对象）
 * @param queueName 队列名称
 * @return 消息对象
 */
public RedisMessage rpopMessage(String queueName) {
    Object result = redisTemplate.opsForList().rightPop(queueName);
    return result instanceof RedisMessage ? (RedisMessage) result : null;
}

/**
 * 阻塞出队（从队列左端取出，阻塞等待）
 * @param queueName 队列名称
 * @param timeout 超时时间（秒）
 * @return 消息内容
 */
public Object blpop(String queueName, long timeout) {
    List<Object> result = Collections.singletonList(redisTemplate.opsForList().leftPop(queueName, timeout, TimeUnit.SECONDS));
    return result != null && !result.isEmpty() ? result.get(1) : null;
}

/**
 * 阻塞出队（从队列左端取出，阻塞等待，转换为消息对象）
 * @param queueName 队列名称
 * @param timeout 超时时间（秒）
 * @return 消息对象
 */
public RedisMessage blpopMessage(String queueName, long timeout) {
    List<Object> result = Collections.singletonList(redisTemplate.opsForList().leftPop(queueName, timeout, TimeUnit.SECONDS));
    if (result != null && !result.isEmpty() && result.get(1) instanceof RedisMessage) {
        return (RedisMessage) result.get(1);
    }
    return null;
}

/**
 * 阻塞出队（从队列右端取出，阻塞等待）
 * @param queueName 队列名称
 * @param timeout 超时时间（秒）
 * @return 消息内容
 */
public Object brpop(String queueName, long timeout) {
    List<Object> result = Collections.singletonList(redisTemplate.opsForList().rightPop(queueName, timeout, TimeUnit.SECONDS));
    return result != null && !result.isEmpty() ? result.get(1) : null;
}

/**
 * 阻塞出队（从队列右端取出，阻塞等待，转换为消息对象）
 * @param queueName 队列名称
 * @param timeout 超时时间（秒）
 * @return 消息对象
 */
public RedisMessage brpopMessage(String queueName, long timeout) {
    List<Object> result = Collections.singletonList(redisTemplate.opsForList().rightPop(queueName, timeout, TimeUnit.SECONDS));
    if (result != null && !result.isEmpty() && result.get(1) instanceof RedisMessage) {
        return (RedisMessage) result.get(1);
    }
    return null;
}

}
