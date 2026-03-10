package com.example.blowords.common.exception;

public class UsernameNotFoundException extends BusinessException {
    public UsernameNotFoundException(String message) {
        super(404, message); // 404 未找到状态码
    }
}
