package com.framework.template.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "error test"),

    // 인증
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "A-001", "유효한 비밀번호가 아닙니다."),
    AUTHENTICATION_PROGRESS(HttpStatus.UNAUTHORIZED, "A-002", "인증 처리 중 오류가 발생하였습니다."),

    // 회원
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-001", "해당 회원은 존재하지 않습니다."),
    MEMBER_EXIST(HttpStatus.BAD_REQUEST, "M-002", "동일한 아이디가 존재합니다.");

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;
}
