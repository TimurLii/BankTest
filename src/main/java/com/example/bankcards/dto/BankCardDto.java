package com.example.bankcards.dto;

import com.example.bankcards.entity.BankCard;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;

public record BankCardDto(
        UserDto userDto,
        @NotNull(message = "validPeriod не должен быть null")
        @FutureOrPresent(message = "validPeriod должен быть в будущем или настоящем")
        Date validPeriod,

        @NotNull(message = "balance не должен быть null")
        @PositiveOrZero(message = "balance должен быть положительным или нулём")
        Long balance,
        @Nullable
        String bankCardNumber,

        @NotNull(message = "statusCard не должен быть null")
        BankCard.StatusCard statusCard

) {
}