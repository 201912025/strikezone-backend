package com.strikezone.strikezone_backend.domain.user.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserExceptionType implements ExceptionType {

    INVALID_SIGN_TOKEN(7001, "유효하지 않은 sign token 입니다."),
    NOT_FOUND_GENDER(7002, "유효하지 않은 gender 형식입니다."),
    NOT_FOUND_TEAM(7003, "유효하지 않은 팀 형식입니다."),
    NOT_FOUND_USER(7004, "유저를 찾을 수 없습니다."),
    DUPLICATED_NICKNAME(7005, "중복된 유저 닉네임입니다."),
    PREVIOUS_REGISTERED_USER(7006, "이전에 회원 가입한 내역이 있습니다."),
    INVALID_NICKNAME(7007, "유효하지 않은 닉네임 형식입니다.");

    private final int statusCode;
    private final String message;
}
