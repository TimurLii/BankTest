package com.example.bankcards.util;

import org.springframework.stereotype.Component;

@Component
public class MaskCardNumber {
    public static String getMaskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 12) {
            return cardNumber;
        }
        int length = cardNumber.length();
        String start = cardNumber.substring(0, 4);
        String end = cardNumber.substring(length - 4);
        return start + "*".repeat(length - 8) + end;
    }
}
