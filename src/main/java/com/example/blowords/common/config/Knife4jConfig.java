package com.example.blowords.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class Knife4jConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blowords接口文档")
                        .version("0.0.0")
                        .description("包含用户认证、词库、词书、个人词单等接口")
                        .contact(new Contact().name("后端开发").email("zhouwuqimc@163.com")));
    }
}
