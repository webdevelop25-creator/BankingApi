package com.marina.bankingapi.auth.dto;
import java.util.UUID;

public record RegisterResponse( UUID id,
                                String customerNumber,
                                String email) {


}
