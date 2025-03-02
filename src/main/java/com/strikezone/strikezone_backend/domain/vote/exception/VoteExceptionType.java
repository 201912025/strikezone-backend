package com.strikezone.strikezone_backend.domain.vote.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteExceptionType implements ExceptionType {
    NOT_FOUND_POLL(4300, "투표 주제를 찾을 수 없습니다."),
    NOT_FOUND_POLLOPTION(4301, "투표 옵션을 찾을 수 없습니다."),
    NOT_FOUND_USER(4302, "사용자를 찾을 수 없습니다."),
    DUPLICATE_VOTE(4303, "중복 투표입니다.");

    private final int statusCode;
    private final String message;
}
