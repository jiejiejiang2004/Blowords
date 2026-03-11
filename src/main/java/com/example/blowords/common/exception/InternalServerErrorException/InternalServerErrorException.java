package com.example.blowords.common.exception.InternalServerErrorException;

import com.example.blowords.common.exception.BusinessException;

public class InternalServerErrorException extends BusinessException {
    public InternalServerErrorException(String message) {
        super(500, message);
    }
}
