package com.example.blowords.User.service;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.blowords.User.dto.UserAccountUpdateDTO;
import com.example.blowords.User.dto.UserPasswordResetDTO;
import com.example.blowords.User.dto.UserPasswordUpdateDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.blowords.User.entity.User;
import com.example.blowords.User.mapper.UserMapper;
import com.example.blowords.Email.service.EmailService;
import com.example.blowords.common.util.PasswordEncryptUtil;
import com.example.blowords.common.util.RedisUtil;
import com.example.blowords.common.util.ValidationUtil;
import com.example.blowords.User.dto.UserAccountDTO;
import org.springframework.util.StringUtils;

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

    @Autowired
    private RedisUtil redisUtil;
    private ValidationUtil validationUtil;
    private final EmailService emailService;
    private final UserMapper userMapper;

    public UserServiceImpl(EmailService emailService, UserMapper userMapper) {
        this.emailService = emailService;
        this.userMapper = userMapper;
        this.validationUtil = new ValidationUtil();
    }

    private static final String AVATAR_DIR = "/api/v1/avatar/getavatar/";

    @Override
    public UserAccountDTO getAccountByUsername(String username) throws UsernameNotFoundException {

        if (!isUserExist(username)) {
            throw new UsernameNotFoundException("用户不存在");
        }

        User user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        UserAccountDTO dto = new UserAccountDTO(user);
        dto.setAvatarUrl(AVATAR_DIR + user.getAvatarUrl());

        return dto;
    }

    private static final String DEFAULT_AVATAR_DIR = "000.jpg";

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

        if (!redisUtil.verifyRegisterCaptcha(email, registerCaptcha)) {
            throw new IllegalArgumentException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, telephone, "USER", LocalDateTime.now(), null, DEFAULT_AVATAR_DIR);

        // 保存用户到数据库
        baseMapper.insert(user);

        String UserID = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));

        return user;
    }

    @Override
    public User register(String username, String password, String email, String registerCaptcha) {

        if (baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        if (baseMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        if (!redisUtil.verifyRegisterCaptcha(email, registerCaptcha)) {
            throw new IllegalArgumentException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        User user = new User(username, password, email, null, "USER", LocalDateTime.now(), null, DEFAULT_AVATAR_DIR);

        baseMapper.insert(user);

        String UserID = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));

        return user;
    }

    @Override
    public String sendRegisterCaptcha(String email, String captcha) {
        if (!ValidationUtil.isValidEmail(email)) {
            return "422"; // 422 - 邮箱格式不正确或不存在
        }

        try {
            User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (existingUser != null && existingUser.getEmail().equals(email)) {
                return "409"; // 409 - 邮箱已注册
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "500"; // 500 - 服务器内部错误
        }

        String redisKey = "email:registerCaptcha:" + email;
        try {
            if (redisUtil.exists(redisKey)) {
                return "429"; // 429 - 请求频繁
            }

            emailService.sendRegisterVerifyCode(email, captcha);
            redisUtil.set(redisKey, captcha, 300);
        } catch (Exception e) {
            e.printStackTrace();
            return "400"; // 422 - 发送失败或请求频繁
        }
        return "200"; // 200 - 发送成功
    }

    @Override
    public User login(String account, String password) {
        User user = null;

        if (validationUtil.isValidEmail(account)) {
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("email", account));
        } else if (validationUtil.isPhoneNumberValid(account)) {
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("telephone", account));
        } else {
            user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", account));
        }

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!PasswordEncryptUtil.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    @Override
    public UserAccountDTO updateAccount(String username, UserAccountUpdateDTO updateDTO) {
        if (!isUserExist(username)) {
            throw new RuntimeException("用户不存在");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().eq("username", username);
        if (StringUtils.hasText(updateDTO.getEmail())) {
            updateWrapper.set("email", updateDTO.getEmail());
            user.setEmail(updateDTO.getEmail()); // 同步更新内存中的user对象，方便后续转DTO
        }
        if (StringUtils.hasText(updateDTO.getAvatarUrl())) {
            updateWrapper.set("avatarurl", updateDTO.getAvatarUrl());
            user.setAvatarUrl(updateDTO.getAvatarUrl());
        }
        if (StringUtils.hasText(updateDTO.getNickname())) {
            updateWrapper.set("nickname", updateDTO.getNickname());
            user.setNickname(updateDTO.getNickname());
        }
        if (StringUtils.hasText(updateDTO.getTelephone())) {
            updateWrapper.set("telephone", updateDTO.getTelephone());
            user.setTelephone(updateDTO.getTelephone());
        }

        userMapper.update(null, updateWrapper);

        user = baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        UserAccountDTO dto = new UserAccountDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    private boolean isUserExist(String account) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", account)
                .or().eq("email", account)
                .or().eq("telephone", account)) != null;
    }

    @Override
    public void updatePassword(String username, UserPasswordUpdateDTO updateDTO) {
        if (!isUserExist(username)) {
            throw new RuntimeException("用户不存在");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        if (!PasswordEncryptUtil.matches(updateDTO.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        SetPassword(username, updateDTO.getNewPassword());
    }

    @Override
    public void SetPassword(String username, String password) {
        String encryptedPassword = PasswordEncryptUtil.encrypt(password);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>().eq("username", username);
        updateWrapper.set("password", encryptedPassword);
        userMapper.update(null, updateWrapper);
    }

    @Override
    public String sendResetPasswordCode(String email, String captcha) {
        if (!ValidationUtil.isValidEmail(email)) {
            return "422"; // 422 - 邮箱格式不正确或不存在
        }

        try {
            User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (existingUser == null) {
                return "409"; // 409 - 邮箱未注册
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "500"; // 500 - 服务器内部错误
        }

        String redisKey = "email:resetPasswordCaptcha:" + email;
        try {
            if (redisUtil.exists(redisKey)) {
                return "429"; // 429 - 请求频繁
            }
            emailService.sendResetPasswordCode(email, captcha);
            redisUtil.set(redisKey, captcha, 300);
        } catch (Exception e) {
            e.printStackTrace();
            return "400"; // 422 - 发送失败或请求频繁
        }
        return "200"; // 200 - 发送成功
    }
}
