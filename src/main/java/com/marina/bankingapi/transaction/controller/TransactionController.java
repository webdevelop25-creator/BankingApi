package com.marina.bankingapi.transaction.controller;

import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import com.marina.bankingapi.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor

public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionResponse deposit(@Valid @RequestBody DepositRequest request) {
        return transactionService.deposit(request);
    }
    @PostMapping("/withdraw")
    public TransactionResponse withdraw(@Valid @RequestBody WithdrawalRequest request) {
        return transactionService.withdraw(request);
    }
}
