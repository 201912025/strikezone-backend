package com.strikezone.strikezone_backend.domain.polloption.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PollOptionExceptionType implements ExceptionType {
    NOT_FOUND_POLLOPTION(4200, "투표 옵션을 찾을 수 없습니다."),
    NOT_FOUND_POLL_FOR_OPTION(4201, "투표 주제를 찾을 수 없습니다.");

    private final int statusCode;
    private final String message;
}
