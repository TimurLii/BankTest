package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtResponse(
        @NotBlank(message = "token не должен быть пустым")
        String token
) {}