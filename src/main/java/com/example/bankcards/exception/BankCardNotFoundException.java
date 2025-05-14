package com.example.bankcards.exception;

public class BankCardNotFoundException extends RuntimeException{


    public BankCardNotFoundException(String message) {
        super(message);
    }

    public BankCardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BankCardNotFoundException(Throwable cause) {
        super(cause);
    }

    public BankCardNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
