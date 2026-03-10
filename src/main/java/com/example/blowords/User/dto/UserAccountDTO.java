package com.example.blowords.User.dto;

import com.example.blowords.User.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    private Integer id;
    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private String role;
    private String telephone;

    public  UserAccountDTO(User user) {
        this.id = user.getUserid();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.avatarUrl = user.getAvatarUrl();
        this.role = user.getRole();
        this.telephone = user.getTelephone();
    }
}