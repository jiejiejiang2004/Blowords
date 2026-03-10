package com.example.blowords.User.controller;

// import java.util.Map;
import java.util.HashMap;
import java.util.Map;

import com.example.blowords.User.dto.UserAccountUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.blowords.common.response.ApiResponse;
import com.example.blowords.common.response.LoginResponse;
import com.example.blowords.User.entity.User;
import com.example.blowords.User.service.UserService;
import com.example.blowords.common.util.JwtUtil;
import com.example.blowords.common.util.RedisUtil;
import com.example.blowords.common.util.ValidationUtil;
import com.example.blowords.common.util.VerifyCodeUtil;
import com.example.blowords.User.dto.UserAccountDTO;

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
    public ResponseEntity<ApiResponse<?>> getRegisterCaptcha(@RequestBody Map<String, String> registerCaptchaRequest) {
        String email = registerCaptchaRequest.get("email");

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);

        if (!ValidationUtil.isValidEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(422, "邮箱格式错误", response));
        }

        String captcha = VerifyCodeUtil.generateCode(6);

        String result = userService.sendRegisterCaptcha(email, captcha);

        return switch (result) {
            case "200" -> ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("验证码发送成功"));
            case "422" -> ResponseEntity.badRequest()
                    .body(ApiResponse.error(422, "验证码发送失败或请求频繁", response));
            case "429" -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(ApiResponse.error(429, "请求频繁，请稍后再试", response));
            case "409" -> ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "邮箱已注册", response));
            default -> {
                User user = userService.getUserByEmail(email);
                response.put("user", user);
                yield ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ApiResponse.error(409, "邮箱已注册", response));
            }
        };
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String email = registerRequest.get("email");
        String telephone = registerRequest.get("telephone");
        String registerCaptcha = registerRequest.get("captcha");

        if (username == null || password == null || email == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "请求参数不完整"));
        }

        if (!ValidationUtil.isValidEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(422, "邮箱格式错误"));
        }

        try {
            if(telephone == null) {
                userService.register(username, password, email, registerCaptcha);
            } else {
                userService.register(username, password, email, telephone, registerCaptcha);
            }
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("注册成功"));
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("注册异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()+"666何意味"));
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

                LoginResponse response = new LoginResponse();

                response.setUid(user.getUserid().toString());
                response.setUsername(user.getUsername());
                response.setPhoneNumber(user.getTelephone());
                response.setEmail(user.getEmail());
                response.setNickname(user.getNickname() != null ? user.getNickname() : user.getUsername()); // 如果没有昵称，使用用户名
                response.setAvatarUrl(user.getAvatarUrl() != null ? user.getAvatarUrl() : "/avatarUrl/u" + String.format("%04d", user.getUserid()) + ".png"); // 默认头像
                response.setAccessToken(accessToken);
                response.setRefreshToken(refreshToken);

                redisUtil.set("refreshToken:" + user.getUsername(), refreshToken, jwtUtil.getRefreshExpirationTime());
                redisUtil.set("accessToken:" + user.getUsername(), accessToken, jwtUtil.getAccessExpirationTime());

                return ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.success("登录成功", response, false));
            } else {
                // 登录失败
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.error(401, "登录失败"));
            }
        } catch (Exception e) {
            // 处理异常，返回错误响应
             Logger logger = LoggerFactory.getLogger(UserController.class);
            logger.error("登录异常：", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/me/account")
    public ResponseEntity<ApiResponse<?>> getAccountInfo(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserAccountDTO account = userService.getAccountByUsername(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(account));
    }

    // PATCH：更新账户信息（核心新增接口）
    @PatchMapping("/me/account")
    public ResponseEntity<ApiResponse<?>> updateAccountInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody UserAccountUpdateDTO updateDTO) { // @Validated校验参数

        // 调用Service更新（用户名从Token中获取，避免前端传参篡改）
        UserAccountDTO updatedAccount = userService.updateAccount(userDetails.getUsername(), updateDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedAccount));
    }
}