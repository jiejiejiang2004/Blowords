package com.example.blowords.common.exception.ResourceNotFoundException;

import com.example.blowords.common.exception.BusinessException;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(404, message);
    }
}
