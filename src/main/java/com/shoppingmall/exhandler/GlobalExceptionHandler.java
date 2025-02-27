package com.shoppingmall.exhandler;


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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Map<String, Object> response = new HashMap<>();

    // 중복 예외 핸들러
    // Duplicate 예외는 전부 여기서 처리 가능
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String, Object>> DuplicateExceptionHandler(DuplicateException e) {
        response.clear();
        response.put("status", "error");
        response.put("error", e.getErrors());
        return ResponseEntity.badRequest().body(response);
    }

    // UsernameNotFoundException 예외는 전부 여기서 처리 가능
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> UsernameNotFoundExceptionHandler(
        UsernameNotFoundException e) {
        response.clear();
        response.put("status", "error");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FieldErrorsException.class)
    public ResponseEntity<Map<String, Object>> FieldErrorsExceptionHandler(FieldErrorsException e) {
        response.clear();
        response.put("status", "error");
        response.put("message", e.getErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> IllegalStateExceptionHandler(IllegalStateException e) {
        response.clear();
        response.put("status", "error");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }


}

