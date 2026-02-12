package com.example.blowords.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author 杰杰酱
 * @since 2026-02-12
 */
@Getter
@Setter
@ToString
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

    @TableField("role")
    @Schema(description = "角色")
    private String role;

    @TableField("create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    public User(Integer userid, String username, String password, String email, String role, LocalDateTime createTime) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createTime = createTime;
    }
}
