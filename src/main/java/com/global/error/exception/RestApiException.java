package com.global.error.exception;

import com.global.error.errorCode.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{

    private final ErrorCode errorCode;

}