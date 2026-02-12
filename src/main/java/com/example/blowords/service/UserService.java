package com.example.blowords.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blowords.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
public interface UserService extends IService<User>, UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username);
}
