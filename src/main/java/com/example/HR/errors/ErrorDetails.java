package com.example.HR.errors;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ErrorDetails {

    private String message;

    private String uri;

    private Date timestamp;

    public ErrorDetails() {
        this.timestamp = new Date();
    }

    public ErrorDetails(String message, String uri) {
        this();
        this.message = message;
        this.uri = uri;

    }
}





