package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BankTransferDto(
        @NotBlank(message = "senders card не может быть пустым")
        @PositiveOrZero
        @Size(min = 12, message = "senders card не может быть меньше 12")
        String sendersCard,
        @NotBlank(message = "recipients card не может быть пустым")
        @PositiveOrZero
        @Size(min = 12, message = "recipients card не может быть меньше 12")
        String recipientsCard,
        @NotBlank(message = "summa  не может быть пустым")
        @PositiveOrZero(message = "summa не может быть меньше нуля")
        Long summa

) {
}
