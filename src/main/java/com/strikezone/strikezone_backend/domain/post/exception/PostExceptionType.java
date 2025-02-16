package com.strikezone.strikezone_backend.domain.post.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostExceptionType implements ExceptionType {

    NOT_FOUND_POST(4000, "게시글을 찾을 수 없습니다."),
    UNAUTHORIZED_USER(4010, "작성자가 아니므로 수정할 수 없습니다."),
    DUPLICATED_TITLE(4020, "중복된 게시글 제목입니다.");

    private final int statusCode;
    private final String message;
}
