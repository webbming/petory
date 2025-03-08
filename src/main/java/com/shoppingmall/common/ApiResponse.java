package com.shoppingmall.common;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {
    private boolean success;
    private String message;
    private Map<String, Object> data;

    public ApiResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ResponseEntity<ApiResponse> success(Map<String, Object> data) {
        return ResponseEntity.ok(new ApiResponse(true, "요청이 성공했습니다.", data));
    }

    public static ResponseEntity<ApiResponse> error(Map<String, Object> data) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "요청이 실패했습니다.", data));
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
