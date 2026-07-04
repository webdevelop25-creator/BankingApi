package com.marina.bankingapi.transaction.dto;
import com.marina.bankingapi.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        BigDecimal amount,
        TransactionType type,
        String description,
        LocalDateTime createdAt
)  {
}
