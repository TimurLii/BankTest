package com.example.bankcards.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CreateBankCardNumber {

    public String getBankCardNumber() {

        StringBuilder bankCardNumber = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 12; i++) {
            bankCardNumber.append(random.nextInt(0, 10));
        }

        return bankCardNumber.toString();
    }
}
