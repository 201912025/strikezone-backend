package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class UnAuthorizedException extends BaseException {

    public UnAuthorizedException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
