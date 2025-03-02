package com.strikezone.strikezone_backend.domain.comment.exception;

import com.strikezone.strikezone_backend.global.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommentExceptionType implements ExceptionType {
    NOT_FOUND_COMMENT(6000, "댓글을 찾을 수 없습니다."),
    DUPLICATED_COMMENT(6010, "중복된 댓글입니다."),
    INVALID_COMMENT_CONTENT(6020, "유효하지 않은 댓글 내용입니다."),
    COMMENT_HAS_ASSOCIATED_REPLIES(6030, "댓글에 연관된 답글이 있어 삭제할 수 없습니다."),
    UNAUTHORIZED_USER(6040, "권한이 없습니다.");

    private final int statusCode;
    private final String message;
}
