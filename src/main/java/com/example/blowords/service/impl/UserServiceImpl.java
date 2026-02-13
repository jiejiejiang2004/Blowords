package com.example.blowords.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blowords.entity.User;
import com.example.blowords.mapper.UserMapper;
import com.example.blowords.mapper.impl.UserMapperImpl;
import com.example.blowords.service.UserService;

import java.time.LocalDateTime;
import com.example.blowords.util.PasswordEncryptUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapperImpl, User> implements UserService {

    // 注入 UserMapper
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用 mapper 查询用户
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        
        // 构建并返回 UserDetails 对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole()) // 注意：roles() 方法会自动添加 "ROLE_" 前缀
                .build();
    }

    @Override
    public User register(String username, String email, String password, String telephone) {
        // 检查用户名是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        if (userMapper.selectOne(new QueryWrapper<User>().eq("telephone", telephone)) != null) {
            throw new IllegalArgumentException("手机号已存在");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, telephone, "USER", LocalDateTime.now());
        
        // 保存用户到数据库
        userMapper.insert(user);

        String UserID = userMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));
        
        return user;
    }

    @Override
    public User register(String username, String email, String password) {
        // 检查用户名是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, null, "USER", LocalDateTime.now());
        
        // 保存用户到数据库
        userMapper.insert(user);

        String UserID = userMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));
        
        return user;
    }
}
