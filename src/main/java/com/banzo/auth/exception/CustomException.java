package com.banzo.auth.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(value = { "cause", "stackTrace", "suppressed", "localizedMessage" })
public class CustomException extends RuntimeException {

    private Integer statusCode;
    private String message;

    public CustomException(HttpStatus httpStatusCode, String message) {
        this.statusCode = httpStatusCode.value();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
