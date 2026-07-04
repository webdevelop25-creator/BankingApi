package com.marina.bankingapi.account.dto;
import com.marina.bankingapi.account.enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public record AccountResponse (
        UUID id,
        String iban,
        BigDecimal balance,
        AccountType accountType,
        boolean active
) {
}
