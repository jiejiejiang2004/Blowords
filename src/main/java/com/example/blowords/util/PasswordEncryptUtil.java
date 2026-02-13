package com.example.blowords.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptUtil {

    // 初始化BCrypt加密器（可调整strength参数，默认10，范围4-31，越高越安全）
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    /**
     * 加密明文密码（注册/重置密码时调用）
     * @param rawPassword 明文密码（前端传过来的，需保证HTTPS传输）
     * @return 加密后的密码字符串（包含盐值，可直接存数据库）
     */
    public static String encrypt(String rawPassword) {
        // 空值校验（避免空指针）
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return PASSWORD_ENCODER.encode(rawPassword);
    }

    /**
     * 验证密码是否匹配（登录时调用）
     * @param rawPassword 前端传入的明文密码（登录时输入的）
     * @param encodedPassword 数据库中存储的加密密码
     * @return true-匹配，false-不匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        // 空值校验
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        // 核心：BCrypt自动提取加密串中的盐值，和明文密码比对
        return PASSWORD_ENCODER.matches(rawPassword, encodedPassword);
    }
}
