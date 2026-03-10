package com.example.blowords.Email;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Component
public class EmailTemplateLoader {

    private final ResourceLoader resourceLoader;

    // 注入Spring的资源加载器
    public EmailTemplateLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String getVerifyCodeTemplate(String templateName, String code) throws Exception {
        // 加载templates/email/verify_code.html文件
        Resource resource = resourceLoader.getResource("classpath:templates/email/" + templateName + ".html");
        // 读取文件内容（指定UTF-8编码，避免中文乱码）
        String templateContent = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        // 替换模板中的${code}为实际验证码
        return templateContent.replace("${code}", code);
    }
}
