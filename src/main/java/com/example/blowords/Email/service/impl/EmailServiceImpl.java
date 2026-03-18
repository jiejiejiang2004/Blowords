package com.example.blowords.Email.service.impl;

import com.example.blowords.Email.service.EmailService;
import com.example.blowords.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.apache.commons.validator.routines.EmailValidator;

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
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public boolean sendEmail(String to, String code, String type) {
        if(!isEmailExists(to)){
            return false;
        }

        switch (type) {
            case "Blowords注册验证码" -> {
                return sendRegisterVerifyCode(to, code);
            }
            case "Blowords密码重置验证码" -> {
                return sendResetPasswordCode(to, code);
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public boolean sendRegisterVerifyCode(String to, String code) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail); // 发件人
            helper.setTo(to);          // 收件人
            helper.setSubject("Blowords注册验证码"); // 邮件标题
            String content = emailTemplateLoader.getVerifyCodeTemplate("verify_code", code);
            helper.setText(content, true); // true表示开启HTML格式
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean sendResetPasswordCode(String to, String code) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail); // 发件人
            helper.setTo(to);          // 收件人
            helper.setSubject("Blowords密码重置验证码"); // 邮件标题
            String content = emailTemplateLoader.getVerifyCodeTemplate("reset_password_code", code);
            helper.setText(content, true); // true表示开启HTML格式
            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEmailExists(String email) {

        if(redisUtil.get("email:exist:" + email) != null) {
            return true;
        }

        try {
            // 提取域名
            String domain = email.split("@")[1];
            if(!EmailValidator.getInstance().isValid(email)) {
                return false;
            }

            // 获取MX记录
            Record[] records = new Lookup(domain, Type.MX).run();
            if (records == null || records.length == 0) {
                return false;
            }

            // 连接到邮件服务器
            String mxServer = ((MXRecord) records[0]).getTarget().toString();
            Socket socket = new Socket(mxServer, 25);
//            socket.connect(new InetSocketAddress(mxServer, 25), 10000); // 3秒连接超时
//            socket.setSoTimeout(10000); // 3秒读取超时

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // 模拟SMTP对话
            in.readLine(); // 服务器欢迎信息
            out.println("HELO localhost");
            in.readLine();
            out.println("MAIL FROM:<test@example.com>");
            in.readLine();
            out.println("RCPT TO:<" + email + ">");
            String response = in.readLine();

            // 关闭连接
            out.println("QUIT");
            in.readLine();
            socket.close();

            // 检查响应码，250表示成功
            if(response.startsWith("250")) {
                redisUtil.set("email:exist:" + email, "1", 60 * 5); // 5分钟缓存
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}