package com.strikezone.strikezone_backend.domain.player.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlayerExceptionType implements ExceptionType {

    NOT_FOUND_PLAYER(4000, "선수를 찾을 수 없습니다."),
    DUPLICATED_PLAYER_NAME(4010, "중복된 선수 이름입니다."),
    INVALID_PLAYER_NAME(4020, "유효하지 않은 선수 이름 형식입니다."),
    PLAYER_HAS_ASSOCIATED_TEAMS(4030, "선수가 소속된 팀이 있어 삭제할 수 없습니다."),
    PLAYER_HAS_ASSOCIATED_STATS(4040, "선수의 기록이 있어 삭제할 수 없습니다.");

    private final int statusCode;
    private final String message;
}
