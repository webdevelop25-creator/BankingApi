package com.marina.bankingapi.transaction.service;
import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;
import com.marina.bankingapi.transaction.dto.TransferRequest;

public interface TransactionService {
    TransactionResponse deposit(DepositRequest request);
    TransactionResponse withdraw(WithdrawalRequest request);
    TransactionResponse transfer(TransferRequest request);
}
