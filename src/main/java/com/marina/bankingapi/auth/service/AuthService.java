package com.marina.bankingapi.auth.service;

import com.marina.bankingapi.auth.dto.RegisterRequest;
import com.marina.bankingapi.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
}
