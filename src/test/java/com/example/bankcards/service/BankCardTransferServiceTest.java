package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.BankTransferDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.repository.BankCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankCardTransferServiceTest {
    @Mock
    private BankCardRepository bankCardRepository;

    @Mock
    private BankCardService bankCardService;

    @Mock
    private UserService userService;

    @InjectMocks
    private BankCardTransferService bankCardTransferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void bankCardTransfer() {
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("user");

        User user = new User();
        user.setEmail("user@mail.ru");
        user.setCardHolderName("user");
        user.setBankCards(new HashSet<>());

        BankCard senderCard= new BankCard();
        senderCard.setId(1L);
        senderCard.setOwner(user);
        senderCard.setValidityPeriod(new Date());
        senderCard.setBalance(1000L);
        senderCard.setBankCardNumber("123456789012");
        senderCard.setStatusCard(BankCard.StatusCard.ACTIVE);

        BankCard recipientCard= new BankCard();
        recipientCard.setId(2L);
        recipientCard.setOwner(user);
        recipientCard.setValidityPeriod(new Date());
        recipientCard.setBalance(1000L);
        recipientCard.setBankCardNumber("123456789011");
        recipientCard.setStatusCard(BankCard.StatusCard.ACTIVE);


        List<BankCardDto> userCards = List.of(
                new BankCardDto(new UserDto(user.getCardHolderName(), user.getEmail()), senderCard.getValidityPeriod(), senderCard.getBalance(), senderCard.getBankCardNumber(), senderCard.getStatusCard()),
                new BankCardDto(new UserDto(user.getCardHolderName(), user.getEmail()), recipientCard.getValidityPeriod(), recipientCard.getBalance(), recipientCard.getBankCardNumber(), recipientCard.getStatusCard())
        );

        BankTransferDto transferDto = new BankTransferDto("123456789012", "123456789011", 300L);

        when(userService.findByCardHolderName("user")).thenReturn(Optional.of(user));
        when(bankCardService.getCardsForUser("user")).thenReturn(ResponseEntity.ok(userCards));
        when(bankCardRepository.findBankCardByBankCardNumber("123456789012")).thenReturn(Optional.of(senderCard));
        when(bankCardRepository.findBankCardByBankCardNumber("123456789011")).thenReturn(Optional.of(recipientCard));
        when(bankCardService.updateBankCard(anyLong(), any(BankCardUpdateDto.class))).thenReturn(ResponseEntity.ok(null));

        ResponseEntity<List<BankCardUpdateDto>> response = bankCardTransferService.bankCardTransfer(transferDto, userDetails);

        assertEquals(200, response.getStatusCodeValue());
        List<BankCardUpdateDto> updates = response.getBody();
        assertNotNull(updates);
        assertEquals(2, updates.size());


        BankCardUpdateDto senderUpdate = updates.stream()
                .filter(dto -> dto.userDto().cardHolderName().equals("user") && dto.balance() == 700L)
                .findFirst()
                .orElse(null);

        BankCardUpdateDto recipientUpdate = updates.stream()
                .filter(dto -> dto.userDto().cardHolderName().equals("user") && dto.balance() == 1300L)
                .findFirst()
                .orElse(null);

        assertNotNull(senderUpdate);
        assertNotNull(recipientUpdate);

        verify(userService).findByCardHolderName("user");
        verify(bankCardService).getCardsForUser("user");
        verify(bankCardService, times(2)).updateBankCard(anyLong(), any(BankCardUpdateDto.class));
    }
}