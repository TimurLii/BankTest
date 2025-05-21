package com.example.bankcards.exception;

/**
 * Exception if bank card not found
 */
public class BankCardNotFoundException extends RuntimeException{
    public BankCardNotFoundException(String message) {
        super(message);
    }
}
