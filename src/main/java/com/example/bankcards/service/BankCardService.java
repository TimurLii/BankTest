package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CreateBankCardNumber;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankCardService {

    private final UserService userService;
    private final BankCardRepository bankCardRepository;
    private final UserRepository userRepository;
    private final CreateBankCardNumber createBankCardNumber;


    public BankCardService(UserService userService, BankCardRepository bankCardRepository, UserRepository userRepository, CreateBankCardNumber createBankCardNumber) {
        this.userService = userService;
        this.bankCardRepository = bankCardRepository;
        this.userRepository = userRepository;
        this.createBankCardNumber = createBankCardNumber;
    }

    public ResponseEntity<BankCardDto> save(BankCardDto bankCardDto) {

        User byCardHolderName = userRepository.findByCardHolderName(bankCardDto.userDto().cardHolderName()).get();

        BankCard bankCard = new BankCard();
        bankCard.setOwner( byCardHolderName);
        bankCard.setStatusCard(bankCardDto.statusCard());
        bankCard.setBalance(bankCardDto.balance());
        bankCard.setValidityPeriod(bankCardDto.validPeriod());
        bankCard.setBankCardNumber(getBankCardNumber());



        bankCardRepository.save(bankCard);

        return ResponseEntity.ok(bankCardDto);
    }

    private String getBankCardNumber() {
        String bankCardNumber;
        do {
            bankCardNumber = createBankCardNumber.getBankCardNumber();
        } while (bankCardRepository.getBankCardsByBankCardNumber(bankCardNumber) != null);
        return bankCardNumber;
    }
}
