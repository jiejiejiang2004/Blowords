package com.example.blowords.controller;

// import java.util.Map;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blowords.common.ApiResponse;
import com.example.blowords.common.LoginResponse;
import com.example.blowords.entity.User;
import com.example.blowords.service.UserService;
import com.example.blowords.util.JwtUtil;
import com.example.blowords.util.RedisUtil;
import com.example.blowords.util.ValidationUtil;
import com.example.blowords.util.VerifyCodeUtil;

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
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // @Autowired
    private final JwtUtil jwtUtil;
    // private final ValidationUtil validationUtil;
    private final RedisUtil redisUtil;

    @Autowired
    public UserController(JwtUtil jwtUtil, UserService userService, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        // this.validationUtil = new ValidationUtil();
        this.redisUtil = redisUtil;
    }

    @PostMapping("/registerCaptcha")
    public ApiResponse<?> getRegisterCaptcha(@RequestBody Map<String, String> registerCaptchaRequest) {
        String email = registerCaptchaRequest.get("email");

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);

        if (!ValidationUtil.isValidEmail(email)) {
            return ApiResponse.error(422, "邮箱格式错误", response);
        }

        String captcha = VerifyCodeUtil.generateCode(6);

        String result = userService.sendRegisterCaptcha(email, captcha);

        return switch (result) {
            case "200" -> ApiResponse.success("验证码发送成功");
            case "422" -> ApiResponse.error(422, "验证码发送失败或请求频繁", response);
            case "429" -> ApiResponse.error(429, "请求频繁，请稍后再试", response);
            case "409" -> ApiResponse.error(409, "邮箱已注册", response);
            default -> {
                User user = userService.getUserByEmail(email);
                response.put("user", user);
                yield ApiResponse.error(409, "邮箱已注册", response);
            }
        };
    }

    @PostMapping("/register")
    public ApiResponse<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String email = registerRequest.get("email");
        String telephone = registerRequest.get("telephone");
        String registerCaptcha = registerRequest.get("captcha");

        if (username == null || password == null || email == null) {
            return ApiResponse.error(400, "请求参数不完整");
        }

        if (!ValidationUtil.isValidEmail(email)) {
            return ApiResponse.error(422, "邮箱格式错误");
        }

        // if (telephone != null && !ValidationUtil.isValidTelephone(telephone)) {
        //     return ApiResponse.error(422, "手机号格式错误");
        // }

        try {
            if(telephone == null) {
                userService.register(username, password, email, registerCaptcha);
            } else {
                userService.register(username, password, email, telephone, registerCaptcha);
            }
            
            return ApiResponse.success("注册成功");
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("注册异常：", e);
            return ApiResponse.error(e.getMessage()+"666何意味");
        }
    }
    
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody Map<String, String> loginRequest) {
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
                
                LoginResponse response = new LoginResponse();
                response.setUid(user.getUserid().toString());
                response.setUsername(user.getUsername());
                response.setPhoneNumber(user.getTelephone());
                response.setEmail(user.getEmail());
                response.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername()); // 如果没有昵称，使用用户名
                response.setAvatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "/avatar/u" + String.format("%04d", user.getUserid()) + ".png"); // 默认头像
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken);

                redisUtil.set("refreshToken:" + user.getUsername(), refreshToken, jwtUtil.getRefreshExpirationTime());
                redisUtil.set("accessToken:" + user.getUsername(), accessToken, jwtUtil.getAccessExpirationTime());

                return ApiResponse.success("登录成功", response);
            } else {
                // 登录失败
                return ApiResponse.error(401, "登录失败");
            }
        } catch (Exception e) {
            // 处理异常，返回错误响应
             Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("登录异常：", e);
            return ApiResponse.error("666何意味" + e.getMessage());
        }
    }
}