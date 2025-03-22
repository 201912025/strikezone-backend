package com.strikezone.strikezone_backend.domain.poll.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PollExceptionType implements ExceptionType {

    NOT_FOUND_POLL(3001, "투표 주제를 찾을 수 없습니다."),
    INVALID_POLL_PERIOD(3010, "투표 기간이 유효하지 않습니다."),
    DUPLICATED_POLL_TITLE(3020, "중복된 투표 제목입니다.");

    private final int statusCode;
    private final String message;
}
