package com.example.HR.errors;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiBaseException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }
}
