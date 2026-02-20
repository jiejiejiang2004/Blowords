package com.example.blowords.controller;

// import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import com.example.blowords.service.UserService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registerCaptcha")
    public ResponseEntity<Map<String, Object>> getRegisterCaptcha(@RequestParam Map<String, String> registerCaptchaRequest) {
        // 调用服务层获取验证码
        String captcha = userService.getRegisterCaptcha(username);

        // 返回验证码
        Map<String, Object> response = new HashMap<>();
        response.put("captcha", captcha);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> registerRequest) {
        // 从请求体中提取注册信息
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String email = registerRequest.get("email");
        String telephone = registerRequest.get("telephone");

        // 校验注册信息（简单校验，实际项目中需完善）
        if (username == null || password == null || email == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // 调用服务层注册用户
        if(telephone == null) {
            userService.register(username, password, email);
        } else {
            userService.register(username, password, email, telephone);
        }

        // 注册成功，返回成功响应
        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String email = loginRequest.get("email");
        String telephone = loginRequest.get("telephone");
        String password = loginRequest.get("password");

        if (userService.) {
            // 登录成功，返回token（实际应使用JWT等生成token）
            Map<String, Object> response = new HashMap<>();
            response.put("token", "dummy-jwt-token");
            return ResponseEntity.ok(response);
        } else {
            // 登录失败
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
