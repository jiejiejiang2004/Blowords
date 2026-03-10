package com.example.blowords.Avatar.controller;

import com.example.blowords.common.response.ApiResponse;
import com.example.blowords.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.example.blowords.Avatar.controller.service.AvatarService;

@RestController
@RequestMapping("/api/v1/avatar")
public class AvatarController {

    @Autowired
    private AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<?>> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        switch (ValidationUtil.isAvatarFileValid(file)) {
            case "文件校验通过" -> {
                // 提取文件扩展名
                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                // 生成唯一文件名
                String userId = SecurityContextHolder.getContext().getAuthentication().getName();
                // 上传文件到指定目录
                avatarService.uploadAvatar(userId, extension, file.getBytes());
            }
            case "只支持jpg、jpeg、png、gif格式的图片" -> {
                return ResponseEntity.badRequest().body(
                        ApiResponse.error(404, "只支持jpg、jpeg、png、gif格式的图片")
                );
            }
            case "文件不能为空" -> {
                return ResponseEntity.badRequest().body(
                        ApiResponse.error(400, "文件不能为空")
                );
            }
            case "文件大小不能超过5MB" -> {
                return ResponseEntity.badRequest().body(
                        ApiResponse.error(400, "文件大小不能超过5MB")
                );
            }
            default -> {
                return ResponseEntity.badRequest().body(
                        ApiResponse.error(400, "未知错误")
                );
            }
        }

        // 返回成功响应
        return ResponseEntity.ok().body(ApiResponse.success("上传成功"));
    }

    @GetMapping("/getavatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable String id) {
        byte[] avatarBytes = avatarService.getAvatar(id);

        if (avatarBytes == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ApiResponse.error(404, "用户头像不存在")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(avatarBytes);
    }
}
