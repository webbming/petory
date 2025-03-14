package com.shoppingmall.exhandler;


import com.shoppingmall.user.dto.ApiResponse;
import com.shoppingmall.user.exception.DuplicateException;
import com.shoppingmall.user.exception.FieldErrorsException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// 전역에서 발생하는 모든 예외를 한곳에서 처리할 수 있게 도와주는 class

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.multipart.MultipartException.class)
    public ResponseEntity<ApiResponse<?>> handleMultipartException(org.springframework.web.multipart.MultipartException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse.error("파일 업로드 중 오류가 발생했습니다: " + e.getMessage()));
    }

    // 중복 예외 핸들러
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicateException(DuplicateException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getErrors()));
    }

    // 필드 에러 예외 핸들러
    @ExceptionHandler(FieldErrorsException.class)
    public ResponseEntity<ApiResponse<?>> handleFieldErrorsException(FieldErrorsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.validationError(e.getErrors()));
    }

    // 사용자 이름 찾을 수 없음 예외 핸들러
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
    }

    // 상태 오류 예외 핸들러
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("서버 오류가 발생했습니다."));
    }
}

