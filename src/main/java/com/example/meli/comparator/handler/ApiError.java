package com.example.meli.comparator.handler;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {
    private int status;
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String path;

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}