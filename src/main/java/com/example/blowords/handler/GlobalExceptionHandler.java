package com.example.blowords.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import com.example.blowords.exception.BusinessException;
import com.example.blowords.exception.UserExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全局异常处理器
 * 捕获并处理系统中的各类异常，返回统一格式的错误响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理业务逻辑异常（自定义异常）
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", ex.getCode());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 状态码
    }

    /**
     * 处理用户名已存在异常
     */
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserExistsException(UserExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 409); // 冲突状态码
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 状态码
    }

    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("运行时异常：", ex);
        response.put("code", 500);
        response.put("message", "服务器内部错误：" + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 状态码
    }

    /**
     * 处理所有异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "系统错误：" + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // 500 状态码
    }
}