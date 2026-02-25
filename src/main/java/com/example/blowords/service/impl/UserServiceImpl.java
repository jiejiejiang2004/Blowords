package com.example.blowords.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blowords.entity.User;
import com.example.blowords.mapper.UserMapper;
import com.example.blowords.service.EmailService;
import com.example.blowords.service.UserService;
import com.example.blowords.util.JwtUtil;
import com.example.blowords.util.PasswordEncryptUtil;
import com.example.blowords.util.RedisUtil;
import com.example.blowords.util.ValidationUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    // // 注入 UserMapper
    // @Autowired
    // private UserMapper userMapper;

    @Autowired
    private RedisUtil redisUtil;

    private ValidationUtil validationUtil;

    // @Autowired
    // private final JwtUtil jwtUtil;

    private final EmailService emailService;

    public UserServiceImpl(EmailService emailService) {
        this.emailService = emailService;
        this.validationUtil = new ValidationUtil();
        // this.jwtUtil = 
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用 mapper 查询用户
        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        
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
    public User register(String username, String password, String email, String telephone, String registerCaptcha) {
        // 检查用户名是否已存在
        if (baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (baseMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        if (baseMapper.selectOne(new QueryWrapper<User>().eq("telephone", telephone)) != null) {
            throw new IllegalArgumentException("手机号已存在");
        }

        if(!redisUtil.verifyRegisterCaptcha(email, registerCaptcha)) {
            throw new IllegalArgumentException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, telephone, "USER", LocalDateTime.now());
        
        // 保存用户到数据库
        baseMapper.insert(user);

        String UserID = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));
        
        return user;
    }

    @Override
    public User register(String username, String password, String email, String registerCaptcha) {
        // 检查用户名是否已存在
        if (baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (baseMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        if(!redisUtil.get("email:captcha:" + email).equals(registerCaptcha)) {
            throw new IllegalArgumentException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, null, "USER", LocalDateTime.now());
        
        // 保存用户到数据库
        baseMapper.insert(user);

        String UserID = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));
        
        return user;
    }

    @Override
    public String sendRegisterCaptcha(String email, String captcha) {
        // 1. 检查邮箱格式是否正确
        if (!ValidationUtil.isValidEmail(email)) {
            return "422"; // 422 - 邮箱格式不正确或不存在
        }

        System.out.println("emall:" + email);
        
        try {
            // 检查邮箱是否已注册
            User existingUser = baseMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (existingUser != null) {
                return "409"; // 409 - 邮箱已注册
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "500"; // 500 - 服务器内部错误
        }
        
        String redisKey = "email:captcha:" + email;
        try {
            if (redisUtil.exists(redisKey)) {
                return "429"; // 429 - 请求频繁
            }
            
            // 4. 发送验证码
            emailService.sendRegisterVerifyCode(email, captcha);
            
            // 5. 存储发送时间戳到Redis，设置冷却时间（例如60秒）
            redisUtil.set(redisKey, captcha, 300);

            if(!redisUtil.exists(redisKey)) {
                return "400"; // 429 - 请求频繁
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return "400"; // 422 - 发送失败或请求频繁
        }
        
        return "200"; // 200 - 发送成功
    }

    @Override
    public User login(String account, String password) {
        User user = null;
        
        // 根据账号类型查询用户
        if(validationUtil.isValidEmail(account)) {
            // 邮箱登录
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("email", account));
        } else if(validationUtil.isPhoneNumberValid(account)) {
            // 手机号登录
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("telephone", account));
        } else {
            // 用户名登录
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", account));
        }
        
        // 检查用户是否存在
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!PasswordEncryptUtil.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("email", email));
    }
}