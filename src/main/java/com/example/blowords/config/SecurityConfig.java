package com.example.blowords.config;

import com.example.blowords.service.impl.UserServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.blowords.filter.JwtAuthenticationFilter;
import com.example.blowords.handler.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthEntryPoint;

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 认证提供者
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // 认证管理器
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 安全过滤链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭CSRF（前后端分离不需要）
                .csrf(csrf -> csrf.disable())
                // 认证失败处理
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))
                // 无状态会话（JWT不需要Session）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 权限控制
                .authorizeHttpRequests(auth -> auth
                        // 允许匿名访问的接口（登录、注册、测试、接口文档）
                        .requestMatchers(
                            "/api/auth/**", 
                            "/api/test/**", 
                            "/webjars/**", 
                            "/v3/**", 
                            "/swagger-resources/**"
                        ).permitAll()
                        // 管理员接口需要ADMIN角色
                        .requestMatchers(
                            "/doc.html/**", 
                            "/api/admin/**"
                        ).hasRole("ADMIN")
                        // 其他接口需要认证
                        .anyRequest().authenticated()
                );

        // 添加JWT过滤器
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}