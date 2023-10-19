package com.global.error.errorCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "로그인 실패. 계정을 확인하세요"),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "인증 토큰이 존재하지 않습니다."),
	SES_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송 시스템에 오류가 발생했습니다."),
	INVALID_AUTHCODE(HttpStatus.BAD_REQUEST, "인증 번호가 일치하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}