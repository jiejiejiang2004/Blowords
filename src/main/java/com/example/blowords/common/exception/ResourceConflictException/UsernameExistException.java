package com.example.blowords.common.exception.ResourceConflictException;

import com.example.blowords.common.exception.BusinessException;

public class UsernameExistException extends ResourceConflictException {
    public UsernameExistException(String message) {
        super(message);
    }
}
