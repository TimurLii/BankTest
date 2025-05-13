package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDto(
        @NotNull(message = "id не должен быть null")
        Long id,

        @NotBlank(message = "cardHolderName не должен быть пустым")
        String cardHolderName
) {}