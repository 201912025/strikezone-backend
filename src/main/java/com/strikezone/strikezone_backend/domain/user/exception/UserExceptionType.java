package com.strikezone.strikezone_backend.domain.user.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserExceptionType implements ExceptionType {

    INVALID_SIGN_TOKEN(2000, "유효하지 않은 sign token 입니다."),
    NOT_FOUND_GENDER(2020, "유효하지 않은 gender 형식입니다."),
    NOT_FOUND_TEAM(2040, "유효하지 않은 팀 형식입니다."),
    NOT_FOUND_USER(2060, "유저를 찾을 수 없습니다."),
    DUPLICATED_NICKNAME(2070, "중복된 유저 닉네임입니다."),
    PREVIOUS_REGISTERED_USER(2080, "이전에 회원 가입한 내역이 있습니다."),
    INVALID_NICKNAME(2090, "유효하지 않은 닉네임 형식입니다.");

    private final int statusCode;
    private final String message;

}
