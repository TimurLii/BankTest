package com.example.bankcards.service;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BankCardService {
    private final BankCardRepository bankCardRepository;
    private final UserRepository userRepository;


    public BankCardService(BankCardRepository bankCardRepository, UserRepository userRepository) {
        this.bankCardRepository = bankCardRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<BankCard> save(BankCard bankCard) {
        return ResponseEntity.ok(bankCardRepository.save(bankCard));
    }
}
