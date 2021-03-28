package com.banzo.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class Error {

    private Integer status;
    private String message;

    public Error() {
        this(HttpStatus.BAD_REQUEST);
    }

    public Error(HttpStatus httpStatus) {
        this(httpStatus, "Something went wrong");
    }

    public Error(HttpStatus httpStatus, String message) {
        this.status = httpStatus.value();
        this.message = message;
    }
}
