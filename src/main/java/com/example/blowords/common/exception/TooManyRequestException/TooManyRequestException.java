package com.example.blowords.common.exception.TooManyRequestException;

import com.example.blowords.common.exception.BusinessException;

public class TooManyRequestException extends BusinessException {
    public TooManyRequestException(String message) {
        super(429, message);
    }
}
