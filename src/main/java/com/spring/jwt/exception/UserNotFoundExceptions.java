package com.spring.jwt.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotFoundExceptions extends RuntimeException {
    private final HttpStatus status;

    public UserNotFoundExceptions(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public UserNotFoundExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

