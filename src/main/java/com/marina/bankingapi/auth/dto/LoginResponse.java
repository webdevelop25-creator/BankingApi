package com.marina.bankingapi.auth.dto;

public record LoginResponse (
        String token,
        String type
) {
}
