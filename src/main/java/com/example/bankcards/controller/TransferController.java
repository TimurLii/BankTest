package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.BankTransferDto;
import com.example.bankcards.impl.UserDetailsImpl;
import com.example.bankcards.service.BankCardTransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransferController {

    private final BankCardTransferService bankCardTransferService;

    public TransferController( BankCardTransferService bankCardTransferService) {
        this.bankCardTransferService = bankCardTransferService;

    }

    @PostMapping("/transfer")
    public ResponseEntity<List<BankCardUpdateDto>> bankCardTransfer(
            @RequestBody @Valid BankTransferDto bankTransferDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return bankCardTransferService.bankCardTransfer(bankTransferDto, userDetails);
    }
}
