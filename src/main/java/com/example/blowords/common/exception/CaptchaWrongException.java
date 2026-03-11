package com.example.blowords.common.exception;

public class CaptchaWrongException extends BusinessException {
    public CaptchaWrongException(String message) {
        super(400, message);
    }
}
