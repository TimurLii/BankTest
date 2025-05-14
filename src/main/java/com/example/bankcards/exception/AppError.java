package com.example.bankcards.exception;

public class AppError {
    private int status;
    private String message;
    private long timestamp;

    public AppError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // геттеры и сеттеры
}