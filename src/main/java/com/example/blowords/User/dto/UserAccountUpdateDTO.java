package com.example.blowords.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.NoArgsConstructor;

/**
 * 更新用户信息的DTO（仅包含可修改的字段，非必填）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountUpdateDTO {
    // 邮箱（可选，带格式校验）
    @Email(message = "邮箱格式不正确")
    private String email;

    // 头像URL（可选，简单格式校验）
    @Pattern(regexp = "^(https?://).*$", message = "头像URL必须以http/https开头")
    private String avatarUrl;

    // 其他可修改字段（比如昵称、手机号等，按需添加）
    private String nickname;

    // 手机号（可选，简单格式校验）
    @Pattern(regexp = "^1[123456789]\\d{9}$", message = "手机号格式不正确")
    private String telephone;




}