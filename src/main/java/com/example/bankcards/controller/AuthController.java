package com.example.bankcards.controller;

import com.example.bankcards.dto.JwtRequest;
import com.example.bankcards.dto.RegistrationUserDto;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody @Valid JwtRequest jwtRequest){
        return authService.createAuthToken(jwtRequest);
    }

    //TODO За создание user отвечает admin
//    @PostMapping("/registration")
//    public ResponseEntity<?> createNewUser(@RequestBody @Valid  RegistrationUserDto registrationUserDto){
//        return authService.createNewUser(registrationUserDto);
//    }
}
