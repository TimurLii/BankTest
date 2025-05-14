package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;

public record RoleDto(
        @NotBlank(message = "role не должен быть пустым")
        String roleName
) {
}
