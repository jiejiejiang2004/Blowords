package com.example.blowords.common.exception.ResourceConflictException;

import com.example.blowords.common.exception.BusinessException;

/**
 * 用户已存在异常
 */
public class UserExistsException extends ResourceConflictException {
    public UserExistsException(String message) {
        super(message); // 409 冲突状态码
    }
}