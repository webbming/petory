package com.shoppingmall.user.exception;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class DuplicateException extends RuntimeException {

    private Map<String, String> errors = new HashMap<>();

    public DuplicateException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
