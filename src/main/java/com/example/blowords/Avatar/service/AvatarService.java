package com.example.blowords.Avatar.service;

import java.io.IOException;

public interface AvatarService {
    public void uploadAvatar(String userId, String extension, byte[] avatarBytes) throws IOException;
    public byte[] getAvatar(String userId);
    public String getAvatarUrl(String userId);
}
