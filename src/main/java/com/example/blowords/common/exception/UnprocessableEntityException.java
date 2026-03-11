package com.example.blowords.common.exception;

public class UnprocessableEntityException extends BusinessException {
    public UnprocessableEntityException(String message) {
        super(422, message);
    }
}
