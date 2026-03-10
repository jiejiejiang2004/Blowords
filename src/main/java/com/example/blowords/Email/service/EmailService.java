package com.example.blowords.Email.service;

public interface EmailService {
    /**
     * 发送注册验证码邮件
     * @param to 收件人邮箱
     * @param code 验证码
     * @return 是否发送成功
     */
    boolean sendRegisterVerifyCode(String to, String code);
}
