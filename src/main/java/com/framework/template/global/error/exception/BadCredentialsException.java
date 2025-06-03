package com.framework.template.global.error.exception;

import com.framework.template.global.error.ErrorCode;

public class BadCredentialsException extends BusinessException{

    public BadCredentialsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
