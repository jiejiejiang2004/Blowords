package com.example.blowords.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
// import lombok.Getter;
// import lombok.Setter;
// import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
// @Getter
// @Setter
// @ToString
@TableName("user")
@Schema(description = "用户对象")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "userid", type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Integer userid;

    @TableField("username")
    @Schema(description = "用户名")
    private String username;

    @TableField("password")
    @Schema(description = "密码")
    private String password;

    @TableField("email")
    @Schema(description = "邮箱")
    private String email;

    @TableField("telephone")
    @Schema(description = "手机号")
    private String telephone;

    @TableField("role")
    @Schema(description = "角色")
    private String role;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @TableField("nickname")
    @Schema(description = "昵称")
    private String nickname;

    @TableField("avatarurl")
    @Schema(description = "头像URL")
    private String avatarUrl;

    public User(Integer userid, String username, String password, String email, String telephone, String role, LocalDateTime createTime) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.createTime = createTime;
    }

    public User(String username, String password, String email, String telephone, String role, LocalDateTime createTime) {
        this.userid = null;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.createTime = createTime;
    }

    public User(Integer userid, String username, String password, String email, String telephone, String role, LocalDateTime createTime, String nickname, String avatarUrl) {
        this.avatarUrl = avatarUrl;
        this.createTime = createTime;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.role = role;
        this.telephone = telephone;
        this.userid = userid;
        this.username = username;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
