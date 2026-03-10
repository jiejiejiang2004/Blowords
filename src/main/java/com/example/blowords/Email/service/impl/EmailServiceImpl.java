package com.example.blowords.Email.service.impl;

import com.example.blowords.Email.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.example.blowords.Email.EmailTemplateLoader;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailTemplateLoader emailTemplateLoader;

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
            String content = emailTemplateLoader.getVerifyCodeTemplate("verify_code", code);
            helper.setText(content, true); // true表示开启HTML格式
            // 4. 发送邮件
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            // 打印异常日志，方便排查问题
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sendResetPasswordCode(String to, String code) {
        // 1. 创建MIME邮件对象（支持HTML格式）
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            // 2. 设置邮件信息
            helper.setFrom(fromEmail); // 发件人
            helper.setTo(to);          // 收件人
            helper.setSubject("Blowords密码重置验证码"); // 邮件标题
            // 3. 邮件内容（HTML格式，更美观）
            String content = emailTemplateLoader.getVerifyCodeTemplate("reset_password_code", code);
            helper.setText(content, true); // true表示开启HTML格式
            // 4. 发送邮件
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            // 打印异常日志，方便排查问题
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}