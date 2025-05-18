package com.example.bankcards.exception;

public class BankCardNotFoundException extends RuntimeException{
    public BankCardNotFoundException(String message) {
        super(message);
    }
}
