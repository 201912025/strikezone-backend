package com.strikezone.strikezone_backend.domain.user.entity;

import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.strikezone.strikezone_backend.domain.user.exception.UserExceptionType.NOT_FOUND_GENDER;

@Getter
@AllArgsConstructor
public enum Gender {
    MALE("남자"), FEMALE("여자");

    private final String value;

    public static Gender toEnum(String gender) {
        return switch (gender.toUpperCase()) {
            case "남자" -> MALE;
            case "여자" -> FEMALE;


            default -> throw new NotFoundException(NOT_FOUND_GENDER);
        };
    }
}
