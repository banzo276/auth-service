package com.banzo.auth.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
