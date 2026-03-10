package com.example.blowords.User.service;

import com.example.blowords.User.dto.UserAccountDTO;
import com.example.blowords.User.dto.UserAccountUpdateDTO;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blowords.User.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
public interface UserService extends IService<User> {

    public User register(String username, String password, String email, String registerCaptcha);

    public User register(String username, String password, String email, String telephone, String registerCaptcha);

    public String sendRegisterCaptcha(String email, String captcha);

    public User login(String account, String password);

    public User getUserByEmail(String email);

    public UserAccountDTO getAccountByUsername(String username);

    public UserAccountDTO updateAccount(String username, UserAccountUpdateDTO updateDTO);
}
