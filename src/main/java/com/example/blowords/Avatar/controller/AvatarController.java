package com.example.blowords.Avatar.controller;

import com.example.blowords.common.exception.InternalServerErrorException.InternalServerErrorException;
import com.example.blowords.common.exception.ResourceNotFoundException.AvatarNotFoundException;
import com.example.blowords.common.response.ApiResponse;
import com.example.blowords.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import com.example.blowords.Avatar.service.AvatarService;

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
        if(ValidationUtil.isAvatarFileValid(file)) {
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            avatarService.uploadAvatar(userId, extension, file.getBytes());
        } else {
            throw new InternalServerErrorException("未知错误");
        }

        return ResponseEntity.ok().body(ApiResponse.success("上传成功"));
    }

    @GetMapping("/getavatar/{id}")
    public ResponseEntity<?> getAvatar(@PathVariable String id) {
        byte[] avatarBytes = avatarService.getAvatar(id);

        if (avatarBytes == null) {
            throw new AvatarNotFoundException(id);
        }

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG).body(avatarBytes);
    }
}
