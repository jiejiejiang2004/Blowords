package com.example.blowords.exception;

/**
 * 业务逻辑异常基类
 */
public class BusinessException extends RuntimeException {
    private int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 400; // 默认 400 错误码
    }

    public int getCode() {
        return code;
    }
}