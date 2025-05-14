package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankTransferDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.impl.UserDetailsImpl;
import com.example.bankcards.service.BankCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransferController {

    private final BankCardService bankCardService;

    public TransferController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<BankCardDto>> bankCardTransfer(
            @RequestBody BankTransferDto bankTransferDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return bankCardService.bankCardTransfer(bankTransferDto, userDetails);
    }
}
