package com.example.blowords.common.exception.UnauthorizedException;

public class UserNotFoundInTokenException extends UnauthorizedException {
    public UserNotFoundInTokenException(String message) {
        super(message);
    }
}
