package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class ForbiddenException extends BaseException {

    public ForbiddenException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
