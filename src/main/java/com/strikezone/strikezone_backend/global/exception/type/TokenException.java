package com.strikezone.strikezone_backend.global.exception.type;

import com.strikezone.strikezone_backend.global.exception.BaseException;
import com.strikezone.strikezone_backend.global.exception.ExceptionType;

public class TokenException extends BaseException {

    public TokenException(ExceptionType exceptionType){
        super(exceptionType);
    }

}
