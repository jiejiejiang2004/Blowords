package com.example.blowords.common.util;

import com.example.blowords.common.model.RedisMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类（封装常用操作）
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // ==================== 通用操作 ====================

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
     * 存储键值对（无过期时间）
     * @param key 键
     * @param value 值
     */
    public void set(String key, Object value) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
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
     * 获取值（泛型）
     * @param key 键
     * @param <T> 类型
     * @return 值
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject(String key) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        return (T) valueOperations.get(key);
    }

    /**
     * 删除键
     * @param key 键
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除键
     * @param keys 键集合
     */
    public void del(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 判断键是否存在
     * @param key 键
     * @return 是否存在
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 设置键的过期时间
     * @param key 键
     * @param expireSeconds 过期时间（秒）
     * @return 是否成功
     */
    public boolean expire(String key, long expireSeconds) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, expireSeconds, TimeUnit.SECONDS));
    }

    /**
     * 获取键的过期时间
     * @param key 键
     * @return 过期时间（秒），-1表示永不过期
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 查找匹配的键
     * @param pattern 模式，如 "user:*"
     * @return 匹配的键集合
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // ==================== 字符串操作 ====================

    /**
     * 自增
     * @param key 键
     * @return 自增后的值
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * 自增指定值
     * @param key 键
     * @param delta 增量
     * @return 自增后的值
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 自减
     * @param key 键
     * @return 自减后的值
     */
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    /**
     * 自减指定值
     * @param key 键
     * @param delta 减量
     * @return 自减后的值
     */
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /**
     * 追加字符串
     * @param key 键
     * @param value 追加的值
     * @return 追加后的长度
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    // ==================== 哈希操作 ====================

    /**
     * 存储哈希字段
     * @param key 键
     * @param hashKey 哈希字段
     * @param value 值
     */
    public void hset(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 批量存储哈希字段
     * @param key 键
     * @param map 哈希字段和值的映射
     */
    public void hset(String key, Map<String, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取哈希字段的值
     * @param key 键
     * @param hashKey 哈希字段
     * @return 值
     */
    public Object hget(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取哈希字段的值（字符串类型）
     * @param key 键
     * @param hashKey 哈希字段
     * @return 值
     */
    public String hgetStr(String key, String hashKey) {
        Object value = redisTemplate.opsForHash().get(key, hashKey);
        return value == null ? null : value.toString();
    }

    /**
     * 获取哈希所有字段和值
     * @param key 键
     * @return 哈希字段和值的映射
     */
    public Map<Object, Object> hgetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 获取哈希所有字段
     * @param key 键
     * @return 哈希字段集合
     */
    public Set<Object> hkeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * 获取哈希所有值
     * @param key 键
     * @return 哈希值集合
     */
    public List<Object> hvals(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    /**
     * 删除哈希字段
     * @param key 键
     * @param hashKeys 哈希字段
     * @return 删除的字段数
     */
    public Long hdel(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

    /**
     * 判断哈希字段是否存在
     * @param key 键
     * @param hashKey 哈希字段
     * @return 是否存在
     */
    public boolean hexists(String key, String hashKey) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().hasKey(key, hashKey));
    }

    /**
     * 获取哈希字段数量
     * @param key 键
     * @return 字段数量
     */
    public Long hsize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    // ==================== 列表操作 ====================

    /**
     * 从列表左端插入
     * @param key 键
     * @param value 值
     * @return 插入后列表长度
     */
    public Long listLpush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从列表左端批量插入
     * @param key 键
     * @param values 值集合
     * @return 插入后列表长度
     */
    public Long lpushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 从列表右端插入
     * @param key 键
     * @param value 值
     * @return 插入后列表长度
     */
    public Long listRpush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从列表右端批量插入
     * @param key 键
     * @param values 值集合
     * @return 插入后列表长度
     */
    public Long rpushAll(String key, Collection<Object> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 从列表左端取出
     * @param key 键
     * @return 值
     */
    public Object listLpop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从列表右端取出
     * @param key 键
     * @return 值
     */
    public Object listRpop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 阻塞从列表左端取出
     * @param key 键
     * @param timeout 超时时间（秒）
     * @return 值
     */
    public Object listBlpop(String key, long timeout) {
        return redisTemplate.opsForList().leftPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 阻塞从列表右端取出
     * @param key 键
     * @param timeout 超时时间（秒）
     * @return 值
     */
    public Object listBrpop(String key, long timeout) {
        return redisTemplate.opsForList().rightPop(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取列表长度
     * @param key 键
     * @return 长度
     */
    public Long len(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表指定范围的元素
     * @param key 键
     * @param start 开始索引
     * @param end 结束索引
     * @return 元素列表
     */
    public List<Object> lrange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 根据索引获取列表元素
     * @param key 键
     * @param index 索引
     * @return 元素
     */
    public Object lindex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 根据索引设置列表元素
     * @param key 键
     * @param index 索引
     * @param value 值
     */
    public void lset(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 删除列表中指定值的元素
     * @param key 键
     * @param count 删除数量，0表示全部
     * @param value 值
     * @return 删除的元素数量
     */
    public Long lrem(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    // ==================== 集合操作 ====================

    /**
     * 添加集合元素
     * @param key 键
     * @param values 值
     * @return 添加的元素数量
     */
    public Long sadd(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合所有元素
     * @param key 键
     * @return 元素集合
     */
    public Set<Object> smembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断元素是否在集合中
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    public boolean sismember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 删除集合元素
     * @param key 键
     * @param values 值
     * @return 删除的元素数量
     */
    public Long srem(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 随机获取集合元素
     * @param key 键
     * @return 元素
     */
    public Object srandmember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 随机获取集合指定数量的元素
     * @param key 键
     * @param count 数量
     * @return 元素列表
     */
    public List<Object> srandmember(String key, long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机弹出集合元素
     * @param key 键
     * @return 元素
     */
    public Object spop(String key) {
        return redisTemplate.opsForSet().pop(key);
    }

    /**
     * 获取集合大小
     * @param key 键
     * @return 大小
     */
    public Long scard(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 集合交集
     * @param key 键
     * @param otherKeys 其他键
     * @return 交集元素
     */
    public Set<Object> sinter(String key, String... otherKeys) {
        return redisTemplate.opsForSet().intersect(key, List.of(otherKeys));
    }

    /**
     * 集合并集
     * @param key 键
     * @param otherKeys 其他键
     * @return 并集元素
     */
    public Set<Object> sunion(String key, String... otherKeys) {
        return redisTemplate.opsForSet().union(key, List.of(otherKeys));
    }

    /**
     * 集合差集
     * @param key 键
     * @param otherKeys 其他键
     * @return 差集元素
     */
    public Set<Object> sdiff(String key, String... otherKeys) {
        return redisTemplate.opsForSet().difference(key, List.of(otherKeys));
    }

    // ==================== 有序集合操作 ====================

    /**
     * 添加有序集合元素
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 是否添加成功
     */
    public Boolean zadd(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加有序集合元素
     * @param key 键
     * @param scoreMembers 分数和值的映射
     * @return 添加的元素数量
     */
    public Long zadd(String key, Set<ZSetOperations.TypedTuple<Object>> scoreMembers) {
        return redisTemplate.opsForZSet().add(key, scoreMembers);
    }

    /**
     * 根据分数范围获取有序集合元素（升序）
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    public Set<Object> zrangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 根据分数范围获取有序集合元素（降序）
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    public Set<Object> zrevrangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 根据排名范围获取有序集合元素（升序）
     * @param key 键
     * @param start 开始排名
     * @param end 结束排名
     * @return 元素集合
     */
    public Set<Object> zrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 根据排名范围获取有序集合元素（降序）
     * @param key 键
     * @param start 开始排名
     * @param end 结束排名
     * @return 元素集合
     */
    public Set<Object> zrevrange(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRange(key, start, end);
    }

    /**
     * 获取有序集合元素的分数
     * @param key 键
     * @param value 值
     * @return 分数
     */
    public Double zscore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取有序集合元素的排名（升序，从0开始）
     * @param key 键
     * @param value 值
     * @return 排名
     */
    public Long zrank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取有序集合元素的排名（降序，从0开始）
     * @param key 键
     * @param value 值
     * @return 排名
     */
    public Long zrevrank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 删除有序集合元素
     * @param key 键
     * @param values 值
     * @return 删除的元素数量
     */
    public Long zrem(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 根据分数范围删除有序集合元素
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 删除的元素数量
     */
    public Long zremrangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
    }

    /**
     * 根据排名范围删除有序集合元素
     * @param key 键
     * @param start 开始排名
     * @param end 结束排名
     * @return 删除的元素数量
     */
    public Long zremrangeByRank(String key, long start, long end) {
        return redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    /**
     * 获取有序集合大小
     * @param key 键
     * @return 大小
     */
    public Long zcard(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    /**
     * 获取有序集合分数范围内的元素数量
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 数量
     */
    public Long zcount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
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
        return redisTemplate.opsForList().leftPop(queueName, timeout, TimeUnit.SECONDS);
    }

    /**
     * 阻塞出队（从队列左端取出，阻塞等待，转换为消息对象）
     * @param queueName 队列名称
     * @param timeout 超时时间（秒）
     * @return 消息对象
     */
    public RedisMessage blpopMessage(String queueName, long timeout) {
        Object result = redisTemplate.opsForList().leftPop(queueName, timeout, TimeUnit.SECONDS);
        return result instanceof RedisMessage ? (RedisMessage) result : null;
    }

    /**
     * 阻塞出队（从队列右端取出，阻塞等待）
     * @param queueName 队列名称
     * @param timeout 超时时间（秒）
     * @return 消息内容
     */
    public Object brpop(String queueName, long timeout) {
        return redisTemplate.opsForList().rightPop(queueName, timeout, TimeUnit.SECONDS);
    }

    /**
     * 阻塞出队（从队列右端取出，阻塞等待，转换为消息对象）
     * @param queueName 队列名称
     * @param timeout 超时时间（秒）
     * @return 消息对象
     */
    public RedisMessage brpopMessage(String queueName, long timeout) {
        Object result = redisTemplate.opsForList().rightPop(queueName, timeout, TimeUnit.SECONDS);
        return result instanceof RedisMessage ? (RedisMessage) result : null;
    }

    /**
     * 获取队列长度
     * @param queueName 队列名称
     * @return 队列长度
     */
    public Long getQueueLength(String queueName) {
        return redisTemplate.opsForList().size(queueName);
    }

    /**
     * 清空队列
     * @param queueName 队列名称
     */
    public void clearQueue(String queueName) {
        redisTemplate.delete(queueName);
    }

    // ==================== 业务相关方法 ====================

    /**
     * 验证注册验证码
     * @param email 邮箱
     * @param captcha 验证码
     * @return 是否验证通过
     */
    public boolean verifyRegisterCaptcha(String email, String captcha) {
        String storedCaptcha = get("email:registerCaptcha:" + email);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }

    /**
     * 验证重设密码验证码
     * @param email 邮箱
     * @param captcha 验证码
     * @return 是否验证通过
     */
    public boolean verifyResetPasswordCaptcha(String email, String captcha) {
        String storedCaptcha = get("email:resetPasswordCaptcha:" + email);
        return storedCaptcha != null && storedCaptcha.equals(captcha);
    }
}