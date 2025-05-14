package com.example.bankcards.dto;

import io.micrometer.common.lang.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public record UpdateUserDto(
        @Nullable
        String cardHolderName,
        @Nullable
        Collection<RoleDto> roleDtoCollection,
        @Nullable
        List<BankCardDto> bankCardDtoList,
        @Nullable
        String email



) {
}
