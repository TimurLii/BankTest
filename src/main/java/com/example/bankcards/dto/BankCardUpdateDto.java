package com.example.bankcards.dto;

import com.example.bankcards.entity.BankCard;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Date;

public record BankCardUpdateDto(
        @Nullable UserDto userDto,
        @Nullable @FutureOrPresent Date validPeriod,
        @Nullable @PositiveOrZero Long balance,
        @Nullable BankCard.StatusCard statusCard
) {}
