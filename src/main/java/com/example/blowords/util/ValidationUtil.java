package com.example.blowords.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 通用参数校验工具类
 */
public class ValidationUtil {
    // 邮箱正则（通用版）
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    // 验证码正则（6位数字）
    private static final Pattern VERIFY_CODE_PATTERN = Pattern.compile("^\\d{6}$");

    /**
     * 校验邮箱格式
     * @param email 待校验的邮箱
     * @return true=格式合法，false=格式非法
     */
    public static boolean isValidEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    /**
     * 校验验证码格式（6位数字）
     * @param code 待校验的验证码
     * @return true=格式合法，false=格式非法
     */
    public static boolean isValidVerificationCode(String code) {
        if (StringUtils.isBlank(code)) {
            return false;
        }
        return VERIFY_CODE_PATTERN.matcher(code).matches();
    }

    /**
     * 校验密码复杂度（至少8位，包含字母+数字）
     * @param password 待校验的密码
     * @return true=复杂度达标，false=不达标
     */
    public static boolean isPasswordComplexEnough(String password) {
        if (StringUtils.isBlank(password) || password.length() < 8) {
            return false;
        }
        // 包含字母和数字的正则
        return password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$");
    }

    /*
     * 校验手机号格式（11位数字，1开头）
     * @param phoneNumber 待校验的手机号
     * @return true=格式合法，false=格式非法
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        // 简单的手机号正则校验（11位数字）
        return phoneNumber.matches("^1\\d{10}$");
    }
}
