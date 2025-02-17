package com.shoppingmall.exhandler;

import com.shoppingmall.user.exception.DuplicateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 중복 예외 핸들러
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Map<String , Object>> DuplicateExceptionHandler(DuplicateException e) {
        Map<String , Object> response = new HashMap<>();
        response.put("status", e.getErrors());
        return ResponseEntity.badRequest().body(response);
    }


}
