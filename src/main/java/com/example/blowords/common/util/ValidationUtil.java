package com.example.blowords.common.util;

import java.io.File;
import java.util.regex.Pattern;

import com.example.blowords.common.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

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

    private static final long MAX_AVATAR_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    /**
     * 校验文件是否为空
     * @param file 待校验的文件
     * @return true=文件为空，false=文件不为空
     */
    public static boolean isFileEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return true;
        } else  {
            return false;
        }
    }

    /**
     * 校验文件大小是否符合要求
     * @param file 待校验的文件
     * @param fileSize 允许的最大文件大小（字节）
     * @return true=文件大小符合要求，false=文件大小超出要求
     */
    public static boolean isFileSizeValid(MultipartFile file,  long fileSize) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        return file.getSize() <= fileSize;
    }

    /**
     * 校验文件扩展名是否符合要求
     * @param fileName 待校验的文件名
     * @param allowedExtensions 允许的扩展名数组
     * @return true=扩展名符合要求，false=扩展名不符合要求
     */
    public static boolean isFileExtensionValid(String fileName, String [] allowedExtensions) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        // 检查文件类型
        String extension = fileName.substring(fileName.lastIndexOf("."));
        boolean isAllowed = false;
        for (String allowedExtension : allowedExtensions) {
            if (extension.equalsIgnoreCase(allowedExtension)) {
                isAllowed = true;
                break;
            }
        }
        return isAllowed;
    }

    /**
     * 校验头像文件是否符合要求（大小5MB，格式jpg、jpeg、png、gif）
     * @param file 待校验的头像文件
     * @return 校验结果信息（通过或具体错误信息）
     */
    public static String isAvatarFileValid(MultipartFile file) {
        if(isFileEmpty(file)) {
            return "文件不能为空";
        }

        if(isFileSizeValid(file, MAX_AVATAR_FILE_SIZE)) {
            return "文件大小不能超过5MB";
        }

        if(!isFileExtensionValid(file.getOriginalFilename(), ALLOWED_EXTENSIONS)) {
            return "只支持jpg、jpeg、png、gif格式的图片";
        }

        return "文件校验通过";
    }
}
