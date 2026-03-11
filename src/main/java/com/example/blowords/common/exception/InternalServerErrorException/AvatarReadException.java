package com.example.blowords.common.exception.InternalServerErrorException;

import com.example.blowords.common.exception.BusinessException;

public class AvatarReadException extends InternalServerErrorException {
    public AvatarReadException(String message) {
        super(message);
    }
}
