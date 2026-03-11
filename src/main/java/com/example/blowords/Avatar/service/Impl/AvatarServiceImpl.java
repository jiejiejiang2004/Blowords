package com.example.blowords.Avatar.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.blowords.Avatar.service.AvatarService;
import com.example.blowords.User.entity.User;
import com.example.blowords.User.mapper.UserMapper;
import com.example.blowords.common.exception.ResourceNotFoundException.AvatarNotFoundException;
import com.example.blowords.common.exception.ResourceNotFoundException.UserNotFoundException;
import com.example.blowords.common.exception.InternalServerErrorException.UpdateAvatarFailException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarServiceImpl implements AvatarService {
    private static final String AVATAR_DIR = "src/main/resources/static/img/avatar/";

    private final UserMapper userMapper;

    public AvatarServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void uploadAvatar(String userId, String extension, byte[] avatarBytes) throws IOException {
        Path dirPath = Paths.get(AVATAR_DIR);
        Files.createDirectories(dirPath);
        // 生成唯一文件名
        String uniqueFilename = userId + extension;
        Path filePath = dirPath.resolve(uniqueFilename);

        // 保存文件
        Files.write(filePath, avatarBytes);

        // 更改数据库
        try {
            User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", userId));
            if (user == null) {
                throw new UserNotFoundException("用户不存在");
            }
            user.setAvatarUrl(uniqueFilename);
            userMapper.updateById(user);
        } catch (Exception e) {
            throw new UpdateAvatarFailException("更新用户头像失败");
        }
    }

    @Override
    public byte[] getAvatar(String userId) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", userId));
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }

        String Filename = user.getAvatarUrl();
        if (Filename == null) {
            throw new AvatarNotFoundException("用户头像不存在");
        }

        try {
            Path filePath = Paths.get(AVATAR_DIR, Filename);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("读取用户头像失败", e);
        }
    }

    @Override
    public String getAvatarUrl(String userId) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("userid", userId));
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }

        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl == null) {
            throw new AvatarNotFoundException("用户头像不存在");
        }

        return avatarUrl;
    }
}
