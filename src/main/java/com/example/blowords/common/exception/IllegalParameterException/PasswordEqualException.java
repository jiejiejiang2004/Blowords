package com.example.blowords.common.exception.IllegalParameterException;

import com.example.blowords.common.exception.BusinessException;

public class PasswordEqualException extends IllegalParameterException {
    public PasswordEqualException(String message) {
        super(message);
    }
}
