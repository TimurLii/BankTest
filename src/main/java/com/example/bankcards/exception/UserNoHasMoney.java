package com.example.bankcards.exception;

public class UserNoHasMoney extends RuntimeException{
    public UserNoHasMoney(String s) {
        super(s);
    }
}
