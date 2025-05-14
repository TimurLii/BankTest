package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record RegistrationUserDto(
        @NotBlank(message = "cardHolderName не должен быть пустым")
        String cardHolderName,

        @NotBlank(message = "password не должен быть пустым")
        String password,

        @NotBlank(message = "passwordConfirm не должен быть пустым")
        String passwordConfirm,
        @NotBlank(message = "email не должен быть пустым")
        String email
) {}