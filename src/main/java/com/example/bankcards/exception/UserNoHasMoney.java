package com.example.bankcards.exception;

/**
 * exception if user no has money for transfer
 */
public class UserNoHasMoney extends RuntimeException{
    public UserNoHasMoney(String s) {
        super(s);
    }
}
