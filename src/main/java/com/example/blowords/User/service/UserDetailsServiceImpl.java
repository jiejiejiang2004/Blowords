package com.example.blowords.User.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blowords.User.entity.User;
import com.example.blowords.common.exception.ResourceNotFoundException.UserNotFoundException;
import com.example.blowords.User.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper; // 直接查数据库，无需经过业务层

    // 固定方法：根据用户名加载认证信息，仅此一个方法
    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFoundException {
        // 1. 从数据库查用户（只查认证必要字段：用户名、密码、权限）
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UserNotFoundException("用户不存在：" + username);
        }

        // 2. 封装成 Spring Security 要求的 UserDetails（只包含认证信息）
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()) // 用户名
                .password(user.getPassword())     // 加密后的密码
                .authorities(user.getRole())      // 权限/角色（如 "ROLE_USER"）
                .accountExpired(false)            // 账户是否过期（固定false即可）
                .accountLocked(false)             // 账户是否锁定
                .credentialsExpired(false)        // 密码是否过期
                .disabled(false)                  // 账户是否禁用
                .build();
    }
}
