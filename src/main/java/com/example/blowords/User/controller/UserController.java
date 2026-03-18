package com.example.blowords.User.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.blowords.common.exception.CaptchaWrongException;
import com.example.blowords.common.exception.IllegalParameterException.IllegalEmailFormulaException;
import com.example.blowords.common.exception.IllegalParameterException.IllegalParameterException;
import com.example.blowords.common.exception.InternalServerErrorException.CaptchaSendFailException;
import com.example.blowords.common.exception.InternalServerErrorException.InternalServerErrorException;
import com.example.blowords.common.exception.ResourceNotFoundException.UserNotFoundException;
import com.example.blowords.User.dto.*;
import com.example.blowords.User.mapper.*;
import com.example.blowords.User.entity.*;
import com.example.blowords.User.service.*;
import com.example.blowords.common.util.*;
import com.example.blowords.common.response.*;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;
    private final UserMapper userMapper;

    @Autowired
    public UserController(JwtUtil jwtUtil, UserService userService, RedisUtil redisUtil, UserMapper userMapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        // this.validationUtil = new ValidationUtil();
        this.redisUtil = redisUtil;
        this.userMapper = userMapper;
    }

    @PostMapping("/registerCaptcha")
    public ResponseEntity<ApiResponse<?>> getRegisterCaptcha(@RequestBody Map<String, String> registerCaptchaRequest) {
        String email = registerCaptchaRequest.get("email");

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);

        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalEmailFormulaException("邮箱格式错误");
        }

        String captcha = VerifyCodeUtil.generateCode(6);

        if(userService.sendRegisterCaptcha(email, captcha)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("验证码发送成功", response));
        } else {
            throw new CaptchaSendFailException("验证码发送失败，请稍后重试");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();
        String email = userRegisterDTO.getEmail();
        String telephone = userRegisterDTO.getTelephone();
        String registerCaptcha = userRegisterDTO.getCaptcha();
        if (username == null || password == null || email == null || registerCaptcha == null) {
            throw new IllegalParameterException("请求参数不完整");
        }

        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalEmailFormulaException("邮箱格式错误");
        }

        try {
            if(telephone == null) {
                userService.register(username, password, email, registerCaptcha);
            } else {
                userService.register(username, password, email, telephone, registerCaptcha);
            }

            Map<String, String> response = new HashMap<>();
            response.put("username", username);
            response.put("email", email);
            response.put("telephone", telephone);
            response.put("password", password);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("注册成功", response));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("注册异常：", e);
            throw new InternalServerErrorException(/*e.getMessage()*/"未知异常,注册失败");
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody Map<String, String> loginRequest) {
        String account = loginRequest.get("account");
        String password = loginRequest.get("password");
        
        try {
            User user = userService.login(account, password);

            if (user != null) {
               UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                       .username(user.getUsername())
                       .password(user.getPassword())
                       .roles(user.getRole())
                       .build();
                                
                String accessToken = jwtUtil.generateToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(userDetails);

                LoginResponse response = getLoginResponse(user, accessToken, refreshToken);

                redisUtil.set("refreshToken:" + user.getUsername(), refreshToken, jwtUtil.getRefreshExpirationTime());
                redisUtil.set("accessToken:" + user.getUsername(), accessToken, jwtUtil.getAccessExpirationTime());

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("登录成功", response, false));
            } else {
                throw new UserNotFoundException("用户不存在");
            }
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("登录异常：", e);
            throw new InternalServerErrorException("未知异常,登录失败");
        }
    }

    private static @NonNull LoginResponse getLoginResponse(User user, String accessToken, String refreshToken) {
        LoginResponse response = new LoginResponse();

        response.setUid(user.getUserid().toString());
        response.setUsername(user.getUsername());
        response.setPhoneNumber(user.getTelephone());
        response.setEmail(user.getEmail());
        response.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername()); // 如果没有昵称，使用用户名
        response.setAvatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "/avatarUrl/u" + String.format("%04d", user.getUserid()) + ".png"); // 默认头像
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);
        return response;
    }

    @GetMapping("/me/account")
    public ResponseEntity<ApiResponse<?>> getAccountInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserAccountDTO account = userService.getAccountByUsername(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(account));
    }

    @PatchMapping("/me/account")
    public ResponseEntity<ApiResponse<?>> updateAccountInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody UserAccountUpdateDTO updateDTO) {

        String username;
        try{
            username = userDetails.getUsername();
        } catch (UserNotFoundException e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("更新账户信息异常：", e);
            throw new UserNotFoundException("用户不存在");
        }

        UserAccountDTO updatedAccount = userService.updateAccount(username, updateDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedAccount));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<?>> updatePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody UserPasswordUpdateDTO updateDTO) {

        String username;
        try{
            username = userDetails.getUsername();
        } catch (UserNotFoundException e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("更新账户信息异常：", e);
            throw new UserNotFoundException("用户不存在");
        }

        userService.updatePassword(username, updateDTO);

        // redis token 失效
        redisUtil.del("accessToken:" + userDetails.getUsername());
        redisUtil.del("refreshToken:" + userDetails.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("oldPassword", updateDTO.getOldPassword());
        response.put("newPassword", updateDTO.getNewPassword());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<?>> resetPassword(
            @Validated @RequestBody UserPasswordResetDTO resetDTO
    ) {
        String email = resetDTO.getEmail();
        String username = resetDTO.getUsername();

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if(!email.equals(user.getEmail())) {
            throw new IllegalArgumentException("用户名与邮箱不匹配");
        }

        String resetCaptcha = resetDTO.getCaptcha();
        if(!redisUtil.verifyResetPasswordCaptcha(email, resetCaptcha)) {
            throw new CaptchaWrongException("验证码错误");
        }

        userService.SetPassword(username, resetDTO.getNewPassword());

        // redis token 失效
        redisUtil.del("accessToken:" + username);
        redisUtil.del("refreshToken:" + username);

        return ResponseEntity.ok(ApiResponse.success("密码重置成功"));
    }

    @PostMapping("/password/resetCaptcha")
    public ResponseEntity<ApiResponse<?>> sendResetCaptcha(
            @Validated @RequestBody UserPasswordResetCaptchaDTO captchaDTO
    ) {
        String email = captchaDTO.getEmail();
        String captcha = VerifyCodeUtil.generateCode(6);

        Map<String, String> response = new HashMap<>();
        response.put("email", email);

        if(userService.sendResetPasswordCode(email, captcha)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("验证码发送成功", response));
        } else {
            throw new InternalServerErrorException("未知错误, 发送密码重置验证码失败");
        }
    }
}