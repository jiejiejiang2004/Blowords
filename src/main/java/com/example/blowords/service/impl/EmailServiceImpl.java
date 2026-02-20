package com.example.blowords.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.blowords.service.EmailService;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    // 发件人邮箱（从配置文件读取，和spring.mail.username一致）
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public boolean sendRegisterVerifyCode(String to, String code) {
        // 1. 创建MIME邮件对象（支持HTML格式）
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            // 2. 设置邮件信息
            helper.setFrom(fromEmail); // 发件人
            helper.setTo(to);          // 收件人
            helper.setSubject("Blowords注册验证码"); // 邮件标题
            // 3. 邮件内容（HTML格式，更美观）
            String content = "<div style='font-family: Arial;'>" +
                    "<h3>你好！</h3>" +
                    "<p>你正在注册背单词项目账号，本次验证码为：<span style='color: #ff0000; font-size: 18px; font-weight: bold;'>" + code + "</span></p>" +
                    "<p>验证码有效期为5分钟，请及时验证，请勿泄露给他人！</p>" +
                    "<p>如果不是你本人操作，请忽略此邮件。</p>" +
                    "</div>";
            helper.setText(content, true); // true表示开启HTML格式
            // 4. 发送邮件
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            // 打印异常日志，方便排查问题
            e.printStackTrace();
            return false;
        }
    }
}
