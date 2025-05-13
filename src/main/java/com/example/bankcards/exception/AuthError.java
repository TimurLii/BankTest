package com.example.bankcards.exception;

import java.util.Date;

public class AuthError {
    private int status;

    private String message;

    private Date timestamp;

    public AuthError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
