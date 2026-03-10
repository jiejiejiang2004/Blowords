package com.example.blowords.Avatar.controller.service;

import java.io.IOException;

public interface AvatarService {
    public void uploadAvatar(String userId, String extension, byte[] avatarBytes) throws IOException;
    public byte[] getAvatar(String userId);
}
