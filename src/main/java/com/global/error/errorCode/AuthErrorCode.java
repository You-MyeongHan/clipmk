package com.global.error.errorCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

	INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "Login failed. Please check your id and password."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}