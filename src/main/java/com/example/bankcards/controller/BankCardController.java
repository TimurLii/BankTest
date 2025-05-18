package com.example.bankcards.controller;

import com.example.bankcards.dto.BankCardDto;

import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.service.BankCardService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

@RestController
public class BankCardController {
    private final BankCardService bankCardService;

    public BankCardController(BankCardService bankCardService) {
        this.bankCardService = bankCardService;
    }

    @PostMapping(value = "/cards", produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> createBankCard(@RequestBody @Valid BankCardDto bankCardDto) {
        return bankCardService.save(bankCardDto);
    }

    @GetMapping("/cards")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public Page<BankCardDto> getCards(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PageableDefault(size = 1) Pageable pageable) {
        String username = userDetails.getUsername();

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return bankCardService.getAllBankCard(pageable);
        } else {
            return bankCardService.getCardsForUser(username ,pageable);
        }
    }


    @DeleteMapping("/cards/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BankCardDto> deleteBankCard(@PathVariable Long id) {
        return bankCardService.deleteBankCardById(id);

    }

    @PatchMapping("/cards/{id}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<BankCardDto> patchBankCard(@PathVariable Long id, @RequestBody @Valid BankCardUpdateDto bankCardUpdateDto) {
        return bankCardService.updateBankCard(id, bankCardUpdateDto);
    }

}


