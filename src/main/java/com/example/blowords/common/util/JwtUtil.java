package com.example.blowords.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.core.env.Environment;


@Component
public class JwtUtil {

    private final Environment environment;
    private String accessSecretKey;
    private long accessExpirationTime;
    private String refreshSecretKey;
    private long refreshExpirationTime;

    public JwtUtil(Environment environment) {
        this.environment = environment;
        loadConfig();
    }

    private void loadConfig() {
        accessSecretKey = environment.getProperty("jwt.access.secret", "默认密钥");
        accessExpirationTime = Long.parseLong(environment.getProperty("jwt.access.expiration", "900000"));
        refreshSecretKey = environment.getProperty("jwt.refresh.secret", "默认刷新密钥");
        refreshExpirationTime = Long.parseLong(environment.getProperty("jwt.refresh.expiration", "604800000"));
    }

    // ========== 对外暴露的过期时间获取方法 ==========
    public long getAccessExpirationTime() {
        return accessExpirationTime;
    }

    public long getRefreshExpirationTime() {
        return refreshExpirationTime;
    }
    
    // ========== 生成Token ==========
    // 生成访问令牌（accessToken）
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessSecretKey, accessExpirationTime);
    }

    // 生成刷新令牌（refreshToken）
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshSecretKey, refreshExpirationTime);
    }

    // 核心创建Token方法（通用）
    private String createToken(Map<String, Object> claims, String subject, String secretKey, long expirationTime) {
        // 兜底：防止secretKey为空（关键！解决空指针）
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT密钥不能为空，请检查配置文件");
        }

        return Jwts.builder()
                .claims(claims) // 0.11.x+新API，替代setClaims
                .subject(subject) // 替代setSubject
                .issuedAt(new Date(System.currentTimeMillis())) // 替代setIssuedAt
                .expiration(new Date(System.currentTimeMillis() + expirationTime)) // 替代setExpiration
                .signWith(getSignInKey(secretKey)) // 签名
                .compact();
    }

    // ========== 签名密钥生成 ==========
    private SecretKey getSignInKey(String secretKey) {
        // 兜底：判空（解决你的空指针核心问题）
        if (secretKey == null) {
            throw new NullPointerException("JWT secretKey 为null，请检查配置注入");
        }
        // HS256要求密钥至少32位，你的配置已满足，无需额外处理
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // ========== Token解析/验证（accessToken） ==========
    // 验证accessToken
    public boolean validateToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, accessSecretKey);
    }

    // 从accessToken提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject, accessSecretKey);
    }

    // 从accessToken提取过期时间
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration, accessSecretKey);
    }

    // ========== Token解析/验证（refreshToken） ==========
    // 验证refreshToken（单独方法，避免和accessToken混淆）
    public boolean validateRefreshToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails, refreshSecretKey);
    }

    // 从refreshToken提取用户名
    public String extractUsernameFromRefreshToken(String token) {
        return extractClaim(token, Claims::getSubject, refreshSecretKey);
    }

    // 从refreshToken提取过期时间
    public Date extractRefreshExpiration(String token) {
        return extractClaim(token, Claims::getExpiration, refreshSecretKey);
    }

    // ========== 通用解析方法 ==========
    // 通用验证Token方法
    private boolean validateToken(String token, UserDetails userDetails, String secretKey) {
        try {
            final String username = extractClaim(token, Claims::getSubject, secretKey);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token, secretKey);
        } catch (Exception e) {
            // 捕获所有JWT异常（过期、签名错误、格式错误等）
            return false;
        }
    }

    // 通用提取Claim方法
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String secretKey) {
        final Claims claims = extractAllClaims(token, secretKey);
        return claimsResolver.apply(claims);
    }

    // 通用提取所有Claims（0.11.x+正确API）
    private Claims extractAllClaims(String token, String secretKey) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey(secretKey)) // 0.11.x+新API，替代setSigningKey
                    .build()
                    .parseSignedClaims(token) // 解析签名后的Claims
                    .getPayload(); // 获取载荷（Claims）
        } catch (ExpiredJwtException e) {
            // Token过期时，仍返回Claims（方便判断过期）
            return e.getClaims();
        } catch (Exception e) {
            throw new RuntimeException("解析JWT Token失败：" + e.getMessage(), e);
        }
    }

    // 通用检查Token是否过期
    private boolean isTokenExpired(String token, String secretKey) {
        return extractClaim(token, Claims::getExpiration, secretKey).before(new Date());
    }
}