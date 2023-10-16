package com.global.error.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.global.error.dto.ErrorResponseDto;
import com.global.error.errorCode.CommonErrorCode;
import com.global.error.errorCode.ErrorCode;
import com.global.error.exception.RestApiException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleCustomException(RestApiException e) {
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        log.warn("handleAllException", ex);
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorResponseDto makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponseDto.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponseDto makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorResponseDto.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }

    private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(e, errorCode));
    }

    private ErrorResponseDto makeErrorResponse(BindException e, ErrorCode errorCode) {
        List<ErrorResponseDto.ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ErrorResponseDto.ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponseDto.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }
}