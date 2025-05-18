package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(

        @NotBlank(message = "cardHolderName не должен быть пустым")
        String cardHolderName,
        @NotBlank(message = "email не должен быть пустым")
        String email


) {}