package com.example.blowords.common.exception.ResourceConflictException;

import com.example.blowords.common.exception.BusinessException;

public class EmailExistException extends ResourceConflictException {
    public EmailExistException(String message) {
        super(message);
    }
}
