package com.banzo.auth.exception;

import org.springframework.http.HttpStatus;

public abstract class CustomException extends RuntimeException {

    private HttpStatus statusCode;
    private String message;

    public CustomException(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
