package com.banzo.auth.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RecordNotFoundException.class)
  public ResponseEntity<Object> handleException(RecordNotFoundException exception) {

    log.info("RecordNotFoundException: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleException(BadRequestException exception) {

    log.info("BadRequestException: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<Object> handleException(AccessDeniedException exception) {

    log.info("AccessDeniedException: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception exception) {

    log.error("Unknown error:" + exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
