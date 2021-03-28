package com.banzo.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException exception) {
        return new ResponseEntity<>(new CustomException(
                HttpStatus.resolve(exception.getStatusCode()),
                exception.getMessage()),
                HttpStatus.resolve(exception.getStatusCode()));
    }
}
