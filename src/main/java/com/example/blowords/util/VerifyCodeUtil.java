package com.example.blowords.util;

import java.util.Random;

/**
 * 验证码生成工具类
 */
public class VerifyCodeUtil {
    // 生成6位数字验证码
    public static String generate6DigitCode() {
        Random random = new Random();
        // 生成100000-999999之间的随机数，保证6位
        int code = random.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

    // 可选：生成指定长度的验证码（数字+字母）
    public static String generateCode(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
