package com.example.bankcards.exception;

/**
 * Exception if user has not bank card
 */
public class UserHasNoCardException extends RuntimeException {
    public UserHasNoCardException(String message) {
        super(message);
    }
}
