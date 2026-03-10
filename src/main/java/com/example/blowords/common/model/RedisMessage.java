package com.example.blowords.common.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Redis消息类
 */
public class RedisMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    // 消息ID
    private String id;

    // 消息类型
    private String type;

    // 消息内容
    private Object content;

    // 发送时间
    private LocalDateTime sendTime;

    // 构造方法
    public RedisMessage() {
        this.sendTime = LocalDateTime.now();
    }

    public RedisMessage(String type, Object content) {
        this.type = type;
        this.content = content;
        this.sendTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "RedisMessage{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", content=" + content +
                ", sendTime=" + sendTime +
                '}';
    }
}
