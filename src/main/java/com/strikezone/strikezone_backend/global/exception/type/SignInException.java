package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class SignInException extends BaseException {

    public Object data;
    public SignInException(ExceptionType exceptionType) {
        super(exceptionType);
    }

    public SignInException(ExceptionType exceptionType, Object data) {
        super(exceptionType);
        this.data = data;
    }

}
