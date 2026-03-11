package com.example.blowords.common.exception.ResourceConflictException;

import com.example.blowords.common.exception.BusinessException;

public class ResourceConflictException extends BusinessException {
    public ResourceConflictException(String message) {
        super(409, message);
    }
}
