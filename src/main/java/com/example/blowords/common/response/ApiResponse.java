package com.example.blowords.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 统一API响应类
 * 用于封装API返回的结果，包含状态码、消息和数据
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ApiResponse<T> {
    /**
     * 状态码：200表示成功，其他表示失败
     */
    private int code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * 时间戳（可为null，null时不序列化）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long timestamp;

    public ApiResponse(int code, String message, T data, boolean includeTimestamp) {
        this.code = code;
        this.msg = message;
        this.data = data;
        this.timestamp = includeTimestamp ? System.currentTimeMillis() : null;
    }

    // 构造方法：带时间戳
    public ApiResponse(int code, String message, boolean includeTimestamp) {
        this.code = code;
        this.msg = message;
        this.timestamp = includeTimestamp ? System.currentTimeMillis() : null;
    }

    // 静态方法：成功响应（默认无时间戳）
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "操作成功", data, true);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data, true);
    }

    public static <T> ApiResponse<T> success(T data, boolean includeTimestamp) {
        return new ApiResponse<>(200, "操作成功", data, includeTimestamp);
    }

    public static <T> ApiResponse<T> success(String message, T data, boolean includeTimestamp) {
        return new ApiResponse<>(200, message, data, includeTimestamp);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(200, "操作成功", null, true);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(200, message, null, true);
    }

    // 静态方法：错误响应
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(code, message, null, true);
    }

    public static <T> ApiResponse<T> error(int code, String message, boolean includeTimestamp) {
        return new ApiResponse<>(code, message, includeTimestamp);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(500, message, null, true);
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(code, message, data, true);
    }
}
