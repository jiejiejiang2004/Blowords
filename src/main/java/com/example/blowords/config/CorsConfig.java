package com.example.blowords.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 允许所有前端域名（开发阶段，上线后可指定具体域名）
        config.addAllowedOriginPattern("*");

        // 允许跨域携带Cookie
        config.setAllowCredentials(true);

        // 允许所有请求方法（GET/POST/PUT/DELETE等）
        config.addAllowedMethod("*");

        // 允许所有请求头
        config.addAllowedHeader("*");
        
        // 暴露响应头（前端能获取）
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 所有接口都允许跨域
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}