package com.banzo.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<Object> handleCustomException(CustomException exception) {
    return new ResponseEntity<>(
        new Error(exception.getStatus(), exception.getMessage()), exception.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception exception) {
    return new ResponseEntity<>(new Error(), HttpStatus.BAD_REQUEST);
  }
}
