package com.marina.bankingapi.account.dto;

import com.marina.bankingapi.account.enums.AccountType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;
public record CreateAccountRequest(
        @NotNull UUID userId,
        @NotNull AccountType accountType
)

{
}
