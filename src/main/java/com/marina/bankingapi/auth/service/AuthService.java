package com.marina.bankingapi.auth.service;

import com.marina.bankingapi.auth.dto.RegisterRequest;
import com.marina.bankingapi.auth.dto.RegisterResponse;
import com.marina.bankingapi.auth.dto.LoginRequest;
import com.marina.bankingapi.auth.dto.LoginResponse;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
