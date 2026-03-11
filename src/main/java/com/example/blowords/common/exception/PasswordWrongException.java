package com.example.blowords.common.exception;

public class PasswordWrongException extends BusinessException {
    public PasswordWrongException(String message) {
        super(401, message);
    }
}
