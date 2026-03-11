package com.example.blowords.common.exception.IllegalParameterException;

import com.example.blowords.common.exception.BusinessException;

public class IllegalParameterException extends BusinessException {
    public IllegalParameterException(String message) {
        super(400, message);
    }
}
