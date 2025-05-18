package com.example.bankcards.controller;

import com.example.bankcards.dto.JwtRequest;
import com.example.bankcards.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAuthToken_ReturnsResponseFromService() {
        JwtRequest jwtRequest = new JwtRequest("User", "User");

        when(authService.createAuthToken(any(JwtRequest.class))).thenReturn((ResponseEntity) ResponseEntity.ok("token"));

        ResponseEntity<?> actualResponse = authController.createAuthToken(jwtRequest);

        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertEquals("token", actualResponse.getBody());

        verify(authService, times(1)).createAuthToken(jwtRequest);
    }
}