package com.strikezone.strikezone_backend.domain.team.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamExceptionType implements ExceptionType {

    NOT_FOUND_TEAM(3000, "팀을 찾을 수 없습니다."),
    DUPLICATED_TEAM_NAME(3010, "중복된 팀 이름입니다."),
    INVALID_TEAM_NAME(3020, "유효하지 않은 팀 이름 형식입니다."),
    TEAM_HAS_ASSOCIATED_USERS(3030, "팀에 소속된 유저가 있어 삭제할 수 없습니다."),
    TEAM_HAS_ASSOCIATED_PLAYERS(3040, "팀에 소속된 선수가 있어 삭제할 수 없습니다.");

    private final int statusCode;
    private final String message;
}
