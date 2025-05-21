package com.example.bankcards.exception;

/**
 * Exception authorization
 */
public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }

}
