package com.example.bankcards.controller;

import com.example.bankcards.dto.RegistrationUserDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        RegistrationUserDto registrationUserDto = new RegistrationUserDto(
                "User",
                "user",
                "user",
                "user@mail.ru"
        );
        UserDto userDto = new UserDto(registrationUserDto.cardHolderName(),registrationUserDto.email());

        when(userService.createNewUser(registrationUserDto)).thenReturn
                (ResponseEntity.ok(userDto));

        ResponseEntity<?> actualResponse = userController.createUser(registrationUserDto);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(userDto, actualResponse.getBody());

        verify(userService,times(1)).createNewUser(eq(registrationUserDto));
    }

    @Test
    void getAllBankCard() {

        when(userService.getAllUsers()).thenReturn(ResponseEntity.ok(List.of()));

        ResponseEntity<?> actualResponse = userController.getAllBankCard();

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals(List.of(), actualResponse.getBody());

        verify(userService,times(1)).getAllUsers();
    }

    @Test
    void deleteBankCard() {

        when(userService.deleteUserById(anyLong())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> actualResponse = userController.deleteBankCard(1L);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());

        verify(userService,times(1)).deleteUserById(1L);
    }

    @Test
    void updateUser() {

        when(userService.updateUser(anyLong(), any())).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> actualResponse = userController.updateUser(anyLong(), any());

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());

        verify(userService,times(1)).updateUser(anyLong(), any());
    }
}