package com.marina.bankingapi.transaction.service;
import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;

public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
}
