package com.example.bankcards.service;

import com.example.bankcards.dto.JwtRequest;
import com.example.bankcards.dto.RegistrationUserDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.exception.AuthError;
import com.example.bankcards.util.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAuthToken() {
        JwtRequest jwtRequest = mock(JwtRequest.class);
        when(jwtRequest.cardHolderName()).thenReturn("user");
        when(jwtRequest.password()).thenReturn("pass");

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        ResponseEntity<?> response = authService.createAuthToken(jwtRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertInstanceOf(AuthError.class, response.getBody());
        assertEquals("Неправильный логин или пароль ",  ((AuthError) response.getBody()).getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, never()).loadUserByUsername(anyString());
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    void createNewUser() {
        RegistrationUserDto registrationUserDto = mock(RegistrationUserDto.class);
        when(registrationUserDto.password()).thenReturn("password");
        when(registrationUserDto.passwordConfirm()).thenReturn("password");
        when(registrationUserDto.cardHolderName()).thenReturn("newuser");

        UserDto userDto = new UserDto("newuser", "email@mail.com");
        ResponseEntity<UserDto> userResponse = ResponseEntity.ok(userDto);

        when(userService.findByCardHolderName("newuser")).thenReturn(Optional.empty());
        when(userService.createNewUser(registrationUserDto)).thenReturn(userResponse);

        ResponseEntity<?> response = (ResponseEntity<?>) authService.createNewUser(registrationUserDto).getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response);

        verify(userService, times(1)).findByCardHolderName("newuser");
        verify(userService, times(1)).createNewUser(registrationUserDto);
    }
}