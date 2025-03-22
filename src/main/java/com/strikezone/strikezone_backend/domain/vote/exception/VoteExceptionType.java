package com.strikezone.strikezone_backend.domain.vote.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteExceptionType implements ExceptionType {
    NOT_FOUND_POLL(8001, "투표 주제를 찾을 수 없습니다."),
    NOT_FOUND_POLLOPTION(8002, "투표 옵션을 찾을 수 없습니다."),
    NOT_FOUND_USER(8003, "사용자를 찾을 수 없습니다."),
    DUPLICATE_VOTE(8004, "중복 투표입니다."),
    NOT_VOTED_YET(8005, "사용자가 해당 투표에 참여하지 않았습니다."),
    ALREADY_VOTED(8006, "이미 투표한 사용자입니다.");

    private final int statusCode;
    private final String message;
}
