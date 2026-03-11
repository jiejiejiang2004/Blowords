package com.example.blowords.common.exception.ResourceConflictException;

import com.example.blowords.common.exception.BusinessException;

public class TelephoneExistException extends ResourceConflictException {
    public TelephoneExistException(String message) {
        super(message);
    }
}
