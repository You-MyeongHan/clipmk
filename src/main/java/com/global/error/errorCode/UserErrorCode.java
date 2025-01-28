package com.global.error.errorCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
	NICK_NOT_FOUND(HttpStatus.NOT_FOUND, "닉네임이 입려되지 않았습니다."),
	DUPLICATE_USER(HttpStatus.BAD_REQUEST, "이미 존재하는 계정입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}