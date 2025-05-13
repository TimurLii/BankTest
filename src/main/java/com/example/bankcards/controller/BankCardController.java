package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;

import com.example.bankcards.service.BankCardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Secured("ROLE_ADMIN")  //TODO убрать комментарии  с этой части
public class BankCardController {
    private final BankCardService bankCardService;

    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @PostMapping("/cards")
    public ResponseEntity<?> createBankCard(@RequestBody @Valid BankCardDto bankCardDto){
        return bankCardService.save(bankCardDto);
    }

}


