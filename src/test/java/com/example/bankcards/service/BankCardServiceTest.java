package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.CreateBankCardNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BankCardServiceTest {
    @Mock
    private BankCardRepository bankCardRepository;

    @Mock
    private UserService userService;

    @Mock
    private CreateBankCardNumber createBankCardNumber;

    @InjectMocks
    private BankCardService bankCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save() {
        UserDto userDto = new UserDto("User", "user@mail.ru");
        BankCardDto bankCardDto = new BankCardDto(
                userDto,
                new java.util.Date(),
                1000L,
                null,
                BankCard.StatusCard.ACTIVE
        );

        User user = new User();
        user.setEmail("user@mail.ru");
        user.setCardHolderName("User");
        user.setBankCards(new HashSet<>());

        when(userService.loadUserByEmail("user@mail.ru")).thenReturn(Optional.of(user));

        ResponseEntity<BankCardDto> response = bankCardService.save(bankCardDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bankCardDto, response.getBody());



        verify(userService, times(1)).loadUserByEmail("user@mail.ru");
        verify(userService, times(1)).save(user);

    }

    @Test
    void getAllBankCard() {
        User user = new User();
        user.setCardHolderName("User");
        user.setEmail("user@mail.ru");

        BankCard bankCard = new BankCard();
        bankCard.setOwner(user);
        bankCard.setValidityPeriod(new Date());
        bankCard.setBalance(1000L);
        bankCard.setBankCardNumber("123456789012");
        bankCard.setStatusCard(BankCard.StatusCard.ACTIVE);

        Page<BankCard> page = new PageImpl<>(List.of(bankCard));
        when(bankCardRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<BankCardDto> result = bankCardService.getAllBankCard(PageRequest.of(0, 1));

        assertEquals(1, result.getTotalElements());
        assertEquals("User", result.getContent().get(0).userDto().cardHolderName());
    }

    @Test
    void getCardsForUser() {
        User user = new User();
        user.setCardHolderName("User");
        user.setEmail("user@mail.ru");

        BankCard bankCard = new BankCard();
        bankCard.setOwner(user);
        bankCard.setValidityPeriod(new Date());
        bankCard.setBalance(1000L);
        bankCard.setBankCardNumber("123456789012");
        bankCard.setStatusCard(BankCard.StatusCard.ACTIVE);

        Page<BankCard> page = new PageImpl<>(List.of(bankCard));
        when(userService.findByCardHolderName("User")).thenReturn(Optional.of(user));
        when(bankCardRepository.findByOwner(user, PageRequest.of(0, 1))).thenReturn(page);

        Page<BankCardDto> result = bankCardService.getCardsForUser("User", PageRequest.of(0, 1));

        assertEquals(1, result.getTotalElements());
        assertEquals("User", result.getContent().get(0).userDto().cardHolderName());
    }

    @Test
    void testGetCardsForUser() {
        String username = "user";

        User user = new User();
        user.setCardHolderName(username);
        user.setEmail("user@mail.com");

        BankCard card1 = new BankCard();
        card1.setOwner(user);
        card1.setValidityPeriod(new Date());
        card1.setBalance(1000L);
        card1.setBankCardNumber("123456789012");
        card1.setStatusCard(BankCard.StatusCard.ACTIVE);

        BankCard card2 = new BankCard();
        card2.setOwner(user);
        card2.setValidityPeriod(new Date());
        card2.setBalance(2000L);
        card2.setBankCardNumber("654321098765");
        card2.setStatusCard(BankCard.StatusCard.INACTIVE);

        List<BankCard> cards = List.of(card1, card2);

        when(userService.findByCardHolderName(username)).thenReturn(Optional.of(user));
        when(bankCardRepository.findByOwner(user)).thenReturn(cards);

        ResponseEntity<List<BankCardDto>> response = bankCardService.getCardsForUser(username);

        assertEquals(200, response.getStatusCodeValue());
        List<BankCardDto> dtoList = response.getBody();
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        assertEquals("123456789012", dtoList.get(0).bankCardNumber());
        assertEquals("654321098765", dtoList.get(1).bankCardNumber());

        verify(userService, times(1)).findByCardHolderName(username);
        verify(bankCardRepository, times(1)).findByOwner(user);
    }


    @Test
    void deleteBankCardById() {
        User user = new User();
        user.setCardHolderName("User");
        user.setEmail("user@mail.ru");

        BankCard bankCard = new BankCard();
        bankCard.setId(1L);
        bankCard.setOwner(user);
        bankCard.setValidityPeriod(new Date());
        bankCard.setBalance(1000L);
        bankCard.setBankCardNumber("1234567890123456");
        bankCard.setStatusCard(BankCard.StatusCard.ACTIVE);

        when(bankCardRepository.findById(1L)).thenReturn(Optional.of(bankCard));

        ResponseEntity<BankCardDto> response = bankCardService.deleteBankCardById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User", response.getBody().userDto().cardHolderName());
        verify(bankCardRepository).deleteById(1L);
    }

    @Test
    void updateBankCard() {
        User user = new User();
        user.setCardHolderName("User");
        user.setEmail("user@mail.ru");

        BankCard bankCard = new BankCard();
        bankCard.setId(1L);
        bankCard.setOwner(user);
        bankCard.setValidityPeriod(new Date());
        bankCard.setBalance(1000L);
        bankCard.setBankCardNumber("1234567890123456");
        bankCard.setStatusCard(BankCard.StatusCard.ACTIVE);

        BankCardUpdateDto updateDto = new BankCardUpdateDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                2000L,
                BankCard.StatusCard.INACTIVE
        );

        when(bankCardRepository.findById(1L)).thenReturn(Optional.of(bankCard));
        when(userService.findByCardHolderName("User")).thenReturn(Optional.of(user));

        ResponseEntity<BankCardDto> response = bankCardService.updateBankCard(1L, updateDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User", response.getBody().userDto().cardHolderName());
        assertEquals(2000L, response.getBody().balance());
        assertEquals(BankCard.StatusCard.INACTIVE, response.getBody().statusCard());
        verify(bankCardRepository).save(any(BankCard.class));
    }

    @Test
    void findByBankCardNumber() {
        BankCard card = new BankCard();
        when(bankCardRepository.findBankCardByBankCardNumber("123")).thenReturn(Optional.of(card));
        Optional<BankCard> result = bankCardService.findByBankCardNumber("123");
        assertTrue(result.isPresent());
    }

    @Test
    void updateBankCardsFromDtoList() {
        User user = new User();
        user.setCardHolderName("User");
        user.setEmail("user@mail.ru");

        BankCard bankCard = new BankCard();
        bankCard.setBankCardNumber("1111");
        bankCard.setOwner(user);

        BankCardDto dto = new BankCardDto(
                new UserDto("User", "user@mail.ru"),
                new Date(),
                1000L,
                "1111",
                BankCard.StatusCard.ACTIVE
        );

        when(bankCardRepository.findBankCardByBankCardNumber("1111")).thenReturn(Optional.of(bankCard));

        Set<BankCard> result = bankCardService.updateBankCardsFromDtoList(List.of(dto), user);

        assertEquals(1, result.size());
        verify(bankCardRepository).saveAll(anySet());
    }
}

