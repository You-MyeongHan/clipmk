package com.global.error.errorCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BarterErrorCode implements ErrorCode {

	REGISTRATION_FAILED(HttpStatus.BAD_REQUEST, "상품 등록 실패. 올바른 양식을 제출해주세요."),
	ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 아이템을 찾을 수 없습니다."),
	ITEM_ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "아이템을 수정할 권한이 없습니다."),
	;

    private final HttpStatus httpStatus;
    private final String message;

}