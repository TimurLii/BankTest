package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record JwtRequest(
        @NotBlank(message = "cardHolderName не должен быть пустым")
        String cardHolderName,

        @NotBlank(message = "password не должен быть пустым")
        String password
) {}