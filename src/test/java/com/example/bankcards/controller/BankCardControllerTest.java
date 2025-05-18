package com.example.bankcards.controller;


import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.service.BankCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class BankCardControllerTest {


    @Mock
    private BankCardService bankCardService;

    @InjectMocks
    private BankCardController bankCardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createBankCard() {
        BankCardDto bankCardDto = new BankCardDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                1000L,
                "123456789012",
                BankCard.StatusCard.ACTIVE
        );

        when(bankCardService.save(eq(bankCardDto))).thenReturn(ResponseEntity.ok(bankCardDto));

        ResponseEntity<?> actualResponse = bankCardController.createBankCard(bankCardDto);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(bankCardDto, actualResponse.getBody());

        verify(bankCardService, times(1)).save(eq(bankCardDto));
    }

    @Test
    void getCards() {
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(
                1L, "User", "User", "user@mail.ru",
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                List.of(new BankCard())
        );

        Pageable pageable = PageRequest.of(0, 1);

        BankCardDto bankCardDto = new BankCardDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                1000L,
                "123456789012",
                BankCard.StatusCard.ACTIVE
        );

        Page<BankCardDto> page = new PageImpl<>(List.of(bankCardDto), pageable, 1);

        when(bankCardService.getCardsForUser(eq("User"), eq(pageable))).thenReturn(page);

        Page<BankCardDto> actualPage = bankCardController.getCards(userDetailsImpl, pageable);

        assertEquals(page, actualPage);

        verify(bankCardService, times(1)).getCardsForUser(eq("User"), eq(pageable));
        verify(bankCardService, never()).getAllBankCard(any(Pageable.class));
    }

    @Test
    void deleteBankCard() {
        BankCardDto bankCardDto = new BankCardDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                1000L,
                "123456789012",
                BankCard.StatusCard.ACTIVE
        );

        when(bankCardService.deleteBankCardById(1L)).thenReturn(ResponseEntity.ok(bankCardDto));

        ResponseEntity<?> actualResponse = bankCardController.deleteBankCard(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(bankCardDto, actualResponse.getBody());

        verify(bankCardService, times(1)).deleteBankCardById(1L);
    }

    @Test
    void patchBankCard() {
        BankCardUpdateDto bankCardUpdateDto = new BankCardUpdateDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                1000L,
                BankCard.StatusCard.ACTIVE
        );

        BankCardDto bankCardDto = new BankCardDto(
                bankCardUpdateDto.userDto(),
                bankCardUpdateDto.validPeriod(),
                bankCardUpdateDto.balance(),
                "123456789012",
                bankCardUpdateDto.statusCard()
        );

        when(bankCardService.updateBankCard(1L,bankCardUpdateDto)).thenReturn(ResponseEntity.ok(bankCardDto));

        ResponseEntity<?> actualResponse = bankCardController.patchBankCard(1L, bankCardUpdateDto);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(bankCardDto, actualResponse.getBody());

        verify(bankCardService, times(1)).updateBankCard(1L,bankCardUpdateDto);

    }
}