package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class NotFoundException extends BaseException {

    public NotFoundException(ExceptionType exceptionType) {
        super(exceptionType);
    }

}