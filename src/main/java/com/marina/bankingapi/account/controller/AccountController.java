package com.marina.bankingapi.account.controller;

import com.marina.bankingapi.account.dto.AccountResponse;
import com.marina.bankingapi.account.dto.CreateAccountRequest;
import com.marina.bankingapi.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor

public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }
}
