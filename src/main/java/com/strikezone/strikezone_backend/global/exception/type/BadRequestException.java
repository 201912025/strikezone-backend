package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class BadRequestException extends BaseException {

    public BadRequestException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}
