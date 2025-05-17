package com.example.bankcards.dto;

import jakarta.validation.constraints.*;

public record BankTransferDto(
        @NotBlank(message = "senders card не может быть пустым")
        @Size(min = 12, message = "senders card не может быть меньше 12")
        @Pattern(regexp = "\\d+", message = "sendersCard должен содержать только цифры")
        String sendersCard,
        @NotBlank(message = "recipients card не может быть пустым")
        @Size(min = 12, message = "recipients card не может быть меньше 12")
        @Pattern(regexp = "\\d+", message = "sendersCard должен содержать только цифры")
        String recipientsCard,
        @NotNull(message = "summa  не может быть меньше нуля")
        @PositiveOrZero(message = "summa не может быть меньше нуля")
        Long summa

) {
}
