package com.example.bankcards.exception;

/**
 * Exception if the mail is not in the database
 */
public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException(String emailNotFound) {
        super("Email не найден: " + emailNotFound);
    }
}
