package com.example.blowords.exception;

/**
 * 用户已存在异常
 */
public class UserExistsException extends BusinessException {
    public UserExistsException(String message) {
        super(409, message); // 409 冲突状态码
    }
}