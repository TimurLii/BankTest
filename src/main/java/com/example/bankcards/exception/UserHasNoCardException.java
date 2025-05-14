package com.example.bankcards.exception;

public class UserHasNoCardException extends RuntimeException {
    public UserHasNoCardException(String message) {
        super(message);
    }
}
