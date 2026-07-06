package com.marina.bankingapi.transaction.service;
import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;

public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawalRequest request);
}
