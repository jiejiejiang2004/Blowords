package com.example.blowords.common.handler;

import com.example.blowords.common.exception.IllegalParameterException.IllegalParameterException;
import com.example.blowords.common.exception.InternalServerErrorException.InternalServerErrorException;
import com.example.blowords.common.exception.ResourceConflictException.ResourceConflictException;
import com.example.blowords.common.exception.ResourceNotFoundException.ResourceNotFoundException;
import com.example.blowords.common.exception.TooManyRequestException.TooManyRequestException;
import com.example.blowords.common.exception.UnauthorizedException.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.blowords.common.response.ApiResponse;
import com.example.blowords.common.exception.BusinessException;
import com.example.blowords.common.exception.ResourceConflictException.UserExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理资源冲突异常
     */
    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceConflictException(ResourceConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalParameterException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalParameterException(IllegalParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理资源未找到异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理内部服务器错误异常
     */
    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ApiResponse<?>> handleInternalServerErrorException(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<?>> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理请求频繁的异常
     */
    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ApiResponse<?>> handleTooManyRequestException(TooManyRequestException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理业务逻辑异常（自定义异常）
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(ex.getCode(), ex.getMessage(), false));
    }

    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("运行时异常：", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "服务器内部错误", false));
    }

    /**
     * 处理所有异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        logger.error("系统异常：", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "系统错误", false));
    }
}