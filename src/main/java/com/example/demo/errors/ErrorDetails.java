package com.example.demo.errors;

import java.util.Date;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
