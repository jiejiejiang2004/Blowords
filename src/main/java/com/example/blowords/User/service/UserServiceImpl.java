package com.example.blowords.User.service;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.blowords.User.controller.UserController;
import com.example.blowords.User.dto.UserAccountUpdateDTO;
import com.example.blowords.User.dto.UserPasswordUpdateDTO;
import com.example.blowords.common.exception.CaptchaWrongException;
import com.example.blowords.common.exception.IllegalParameterException.IllegalEmailFormulaException;
import com.example.blowords.common.exception.IllegalParameterException.PasswordEqualException;
import com.example.blowords.common.exception.InternalServerErrorException.InternalServerErrorException;
import com.example.blowords.common.exception.ResourceConflictException.EmailExistException;
import com.example.blowords.common.exception.ResourceConflictException.TelephoneExistException;
import com.example.blowords.common.exception.ResourceConflictException.UserExistsException;
import com.example.blowords.common.exception.ResourceConflictException.UsernameExistException;
import com.example.blowords.common.exception.ResourceNotFoundException.EmailNotFoundException;
import com.example.blowords.common.exception.ResourceNotFoundException.UserNotFoundException;
import com.example.blowords.common.exception.TooManyRequestException.GetTooManyCaptchaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final String DEFAULT_AVATAR_DIR = "000.jpg";

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

    @Override
    public User register(String username, String password, String email, String telephone, String registerCaptcha) {
        // 检查用户名是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new UserExistsException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new EmailExistException("邮箱已存在");
        }

        if (userMapper.selectOne(new QueryWrapper<User>().eq("telephone", telephone)) != null) {
            throw new TelephoneExistException("手机号已存在");
        }

        if (!redisUtil.verifyRegisterCaptcha(email, registerCaptcha)) {
            throw new CaptchaWrongException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        // 创建新用户
        User user = new User(username, password, email, telephone, "USER", LocalDateTime.now(), null, DEFAULT_AVATAR_DIR);

        // 保存用户到数据库
        userMapper.insert(user);

        String UserID = userMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));

        return user;
    }

    @Override
    public User register(String username, String password, String email, String registerCaptcha) {

        if (userMapper.selectOne(new QueryWrapper<User>().eq("username", username)) != null) {
            throw new UsernameExistException("用户名已存在");
        }

        if (userMapper.selectOne(new QueryWrapper<User>().eq("email", email)) != null) {
            throw new EmailExistException("邮箱已存在");
        }

        if (!redisUtil.verifyRegisterCaptcha(email, registerCaptcha)) {
            throw new CaptchaWrongException("验证码错误");
        }

        password = PasswordEncryptUtil.encrypt(password);

        User user = new User(username, password, email, null, "USER", LocalDateTime.now(), null, DEFAULT_AVATAR_DIR);

        userMapper.insert(user);

        String UserID = userMapper.selectOne(new QueryWrapper<User>().eq("username", username)).getUserid().toString();
        user.setUserid(Integer.parseInt(UserID));

        return user;
    }

    @Override
    public boolean sendRegisterCaptcha(String email, String captcha) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalEmailFormulaException("邮箱格式错误");
        }

        if (!emailService.isEmailExists(email)) {
            throw new EmailNotFoundException("邮箱不存在");
        }

        User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
        if (existingUser != null && existingUser.getEmail().equals(email)) {
            throw new IllegalEmailFormulaException("邮箱已注册");
        }

        String redisKey = "email:registerCaptcha:" + email;

        if (redisUtil.exists(redisKey)) {
            throw new GetTooManyCaptchaException("请求频繁,请稍后再试");
        }

        emailService.sendEmail(email, captcha, "Blowords注册验证码");
        redisUtil.set(redisKey, captcha, 300);
        return true;
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
            throw new UserNotFoundException("用户不存在");
        }

        if (!PasswordEncryptUtil.matches(password, user.getPassword())) {
            throw new PasswordEqualException("密码错误");
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
            throw new UserExistsException("用户不存在");
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));

        if (!PasswordEncryptUtil.matches(updateDTO.getOldPassword(), user.getPassword())) {
            throw new PasswordEqualException("旧密码错误");
        }

        if (updateDTO.getOldPassword().equals(updateDTO.getNewPassword())) {
            throw new PasswordEqualException("新密码不能与旧密码相同");
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
    public boolean sendResetPasswordCode(String email, String captcha) {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalEmailFormulaException("邮箱格式错误");
        }

        if (!emailService.isEmailExists(email)) {
            throw new IllegalEmailFormulaException("邮箱不存在");
        }

        try {
            User existingUser = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (existingUser == null) {
                throw new EmailNotFoundException("邮箱未注册");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException("未知错误, 检查邮箱是否注册");
        }

        String redisKey = "email:resetPasswordCaptcha:" + email;
        try {
            if (redisUtil.exists(redisKey)) {
                throw new GetTooManyCaptchaException("获取验证码过于频繁, 请稍后再试");
            }
            emailService.sendEmail(email, captcha, "Blowords密码重置验证码");
            redisUtil.set(redisKey, captcha, 300);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException("未知错误, 发送密码重置验证码失败");
        }
        return true; // 200 - 发送成功
    }
}
