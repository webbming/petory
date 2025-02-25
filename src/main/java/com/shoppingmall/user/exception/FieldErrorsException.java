package com.shoppingmall.user.exception;


import java.util.Map;

public class FieldErrorsException extends RuntimeException {

    private final Map<String, String> errors;

    public FieldErrorsException(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
