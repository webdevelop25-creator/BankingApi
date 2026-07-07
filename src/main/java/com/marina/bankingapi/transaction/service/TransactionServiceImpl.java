package com.marina.bankingapi.transaction.service;

import com.marina.bankingapi.account.repository.AccountRepository;
import com.marina.bankingapi.transaction.dto.TransferRequest;
import com.marina.bankingapi.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.marina.bankingapi.transaction.dto.DepositRequest;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import org.springframework.transaction.annotation.Transactional;
import com.marina.bankingapi.account.entity.Account;
import com.marina.bankingapi.transaction.entity.Transaction;
import com.marina.bankingapi.transaction.enums.TransactionType;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;
import com.marina.bankingapi.common.exception.InsufficientFundsException;


import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class TransactionServiceImpl implements TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        // Logik kommt hier rein
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance().add(request.amount()));

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .type(TransactionType.DEPOSIT)
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .targetAccount(account)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawalRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException();
        }

        account.setBalance(account.getBalance().subtract(request.amount()));

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .type(TransactionType.WITHDRAWAL)
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .sourceAccount(account)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getCreatedAt()
        );
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        Account sourceAccount = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new RuntimeException("Source Account not found"));

        Account targetAccount = accountRepository.findById(request.targetAccountId())
                .orElseThrow(() -> new RuntimeException("Target account not found"));


        if (sourceAccount.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException();
        }
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.amount()));
        targetAccount.setBalance(targetAccount.getBalance().add(request.amount()));

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .type(TransactionType.TRANSFER)
                .description(request.description())
                .createdAt(LocalDateTime.now())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getDescription(),
                savedTransaction.getCreatedAt()
        );

    }
}
