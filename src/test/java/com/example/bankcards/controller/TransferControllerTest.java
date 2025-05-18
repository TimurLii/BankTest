package com.example.bankcards.controller;

import com.example.bankcards.dto.BankTransferDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.service.BankCardTransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransferControllerTest {
    @Mock
    private BankCardTransferService bankCardTransferService;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bankCardTransfer() {
        BankTransferDto bankTransferDto = new BankTransferDto(
                "123456789012",
                "012987654321",
                1000L
                );
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(
                1L, "User", "User", "user@mail.ru",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                List.of(new BankCard())
        );

        when(bankCardTransferService.bankCardTransfer(bankTransferDto, userDetailsImpl)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> actualResponse = transferController.bankCardTransfer(bankTransferDto, userDetailsImpl);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(bankCardTransferService, times(1)).bankCardTransfer(eq(bankTransferDto), eq(userDetailsImpl));
    }
}