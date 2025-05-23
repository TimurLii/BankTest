package com.example.bankcards.controller;

import com.example.bankcards.dto.JwtRequest;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Auth Controller - authorization users
 */
@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * create jwt-token for authorization user
     * @param jwtRequest - dto for  authorization users
     * @return ResponseEntity  jwt-token
     */
    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest jwtRequest){
        return authService.createAuthToken(jwtRequest);
    }

}
