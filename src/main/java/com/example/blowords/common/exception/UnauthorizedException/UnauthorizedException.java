package com.example.blowords.common.exception.UnauthorizedException;

import com.example.blowords.common.exception.BusinessException;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String message) {
        super(401, message);
    }
}
