package com.marina.bankingapi.auth.controller;

import com.marina.bankingapi.auth.dto.RegisterRequest;
import com.marina.bankingapi.auth.dto.RegisterResponse;
import com.marina.bankingapi.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.*;
import com.marina.bankingapi.auth.dto.LoginRequest;
import com.marina.bankingapi.auth.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
