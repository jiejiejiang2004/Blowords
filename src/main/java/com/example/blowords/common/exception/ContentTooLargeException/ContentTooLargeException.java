package com.example.blowords.common.exception.ContentTooLargeException;

import com.example.blowords.common.exception.BusinessException;

public class ContentTooLargeException extends BusinessException {
    public ContentTooLargeException(String message) {
        super(413, message);
    }
}
