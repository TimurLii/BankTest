package com.example.bankcards.exception;

/**
 * Exception if the mail exists in the database
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email уже существует: " + email);
    }
}
