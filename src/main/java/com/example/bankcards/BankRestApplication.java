package com.example.bankcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankRestApplication {
    //TODO сделать присваивание карты пользователю необходимостью при создании карты
    public static void main(String[] args) {
        SpringApplication.run(BankRestApplication.class, args);
    }
}
