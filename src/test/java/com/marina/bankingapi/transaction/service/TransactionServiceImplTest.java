package com.marina.bankingapi.transaction.service;

import com.marina.bankingapi.account.repository.AccountRepository;
import com.marina.bankingapi.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.marina.bankingapi.account.entity.Account;
import com.marina.bankingapi.transaction.dto.TransactionResponse;
import com.marina.bankingapi.transaction.dto.WithdrawalRequest;
import com.marina.bankingapi.transaction.entity.Transaction;
import com.marina.bankingapi.transaction.enums.TransactionType;
import org.junit.jupiter.api.Assertions;
import com.marina.bankingapi.common.exception.InsufficientFundsException;
import com.marina.bankingapi.transaction.dto.TransferRequest;
import com.marina.bankingapi.transaction.dto.DepositRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class TransactionServiceImplTest {

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        transactionRepository = Mockito.mock(TransactionRepository.class);

        transactionService = new TransactionServiceImpl(
                accountRepository,
                transactionRepository
        );
    }

    // Prüfen ob Auszahlung (Withdrawal) erfolgreich ist

    @Test
    void shouldWithdrawMoneyWhenBalanceIsSufficient() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .balance(new BigDecimal("100.00"))
                .active(true)
                .build();

        WithdrawalRequest request = new WithdrawalRequest(
                accountId,
                new BigDecimal("30.00"),
                "ATM withdrawal"
        );

        Transaction savedTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("30.00"))
                .type(TransactionType.WITHDRAWAL)
                .description("ATM withdrawal")
                .createdAt(LocalDateTime.now())
                .sourceAccount(account)
                .build();

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponse response = transactionService.withdraw(request);

        Assertions.assertEquals(new BigDecimal("70.00"), account.getBalance());
        Assertions.assertEquals(TransactionType.WITHDRAWAL, response.type());
        Assertions.assertEquals(new BigDecimal("30.00"), response.amount());

        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));

    }

    // Prüfen Withdrawal → → InsufficientFundsException
    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .balance(new BigDecimal("20.00"))
                .active(true)
                .build();

        WithdrawalRequest request = new WithdrawalRequest(
                accountId,
                new BigDecimal("50.00"),
                "ATM withdrawal"
        );

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        Assertions.assertThrows(
                InsufficientFundsException.class,
                () -> transactionService.withdraw(request)
        );

        Assertions.assertEquals(
                new BigDecimal("20.00"),
                account.getBalance()
        );

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }

    // Prüfen, Transfer zwischen Accounts erfolgreich
    @Test
    void shouldTransferMoneyBetweenAccounts() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();

        Account sourceAccount = Account.builder()
                .id(sourceAccountId)
                .balance(new BigDecimal("100.00"))
                .active(true)
                .build();

        Account targetAccount = Account.builder()
                .id(targetAccountId)
                .balance(new BigDecimal("20.00"))
                .active(true)
                .build();

        TransferRequest request = new TransferRequest(
                sourceAccountId,
                targetAccountId,
                new BigDecimal("30.00"),
                "Transfer to savings"
        );

        Transaction savedTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("30.00"))
                .type(TransactionType.TRANSFER)
                .description("Transfer to savings")
                .createdAt(LocalDateTime.now())
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .build();

        Mockito.when(accountRepository.findById(sourceAccountId))
                .thenReturn(Optional.of(sourceAccount));

        Mockito.when(accountRepository.findById(targetAccountId))
                .thenReturn(Optional.of(targetAccount));

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponse response = transactionService.transfer(request);

        Assertions.assertEquals(
                new BigDecimal("70.00"),
                sourceAccount.getBalance()
        );

        Assertions.assertEquals(
                new BigDecimal("50.00"),
                targetAccount.getBalance()
        );

        Assertions.assertEquals(
                TransactionType.TRANSFER,
                response.type()
        );

        Assertions.assertEquals(
                new BigDecimal("30.00"),
                response.amount()
        );

        Mockito.verify(transactionRepository)
                .save(Mockito.any(Transaction.class));
    }

    //Prüfen ob Deposit erfolgreich
    @Test
    void shouldDepositMoneyIntoAccount() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .balance(new BigDecimal("20.00"))
                .active(true)
                .build();

        DepositRequest request = new DepositRequest(
                accountId,
                new BigDecimal("30.00"),
                "Initial deposit"
        );

        Transaction savedTransaction = Transaction.builder()
                .id(UUID.randomUUID())
                .amount(new BigDecimal("30.00"))
                .type(TransactionType.DEPOSIT)
                .description("Initial deposit")
                .createdAt(LocalDateTime.now())
                .targetAccount(account)
                .build();

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                .thenReturn(savedTransaction);

        TransactionResponse response = transactionService.deposit(request);

        Assertions.assertEquals(
                new BigDecimal("50.00"),
                account.getBalance()
        );

        Assertions.assertEquals(
                TransactionType.DEPOSIT,
                response.type()
        );

        Assertions.assertEquals(
                new BigDecimal("30.00"),
                response.amount()
        );

        Mockito.verify(transactionRepository)
                .save(Mockito.any(Transaction.class));
    }

    //Prüfen Transfer → InsufficientFundsException
    @Test
    void shouldThrowExceptionWhenTransferBalanceIsInsufficient() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();

        Account sourceAccount = Account.builder()
                .id(sourceAccountId)
                .balance(new BigDecimal("20.00"))
                .active(true)
                .build();

        Account targetAccount = Account.builder()
                .id(targetAccountId)
                .balance(new BigDecimal("10.00"))
                .active(true)
                .build();

        TransferRequest request = new TransferRequest(
                sourceAccountId,
                targetAccountId,
                new BigDecimal("50.00"),
                "Transfer to savings"
        );

        Mockito.when(accountRepository.findById(sourceAccountId))
                .thenReturn(Optional.of(sourceAccount));

        Mockito.when(accountRepository.findById(targetAccountId))
                .thenReturn(Optional.of(targetAccount));

        Assertions.assertThrows(
                InsufficientFundsException.class,
                () -> transactionService.transfer(request)
        );

        Assertions.assertEquals(
                new BigDecimal("20.00"),
                sourceAccount.getBalance()
        );

        Assertions.assertEquals(
                new BigDecimal("10.00"),
                targetAccount.getBalance()
        );

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }

    // Prüfen ob Transfer scheitert, wenn das Quellkonto nicht existiert
    @Test
    void shouldThrowExceptionWhenSourceAccountDoesNotExist() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();

        TransferRequest request = new TransferRequest(
                sourceAccountId,
                targetAccountId,
                new BigDecimal("20.00"),
                "Transfer"
        );

        Mockito.when(accountRepository.findById(sourceAccountId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );

        Assertions.assertEquals(
                "Source account not found",
                exception.getMessage()
        );

        Mockito.verify(
                accountRepository,
                Mockito.never()
        ).findById(targetAccountId);

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }

    //Das Zielkonto existiert nicht
    @Test
    void shouldThrowExceptionWhenTargetAccountDoesNotExist() {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();

        Account sourceAccount = Account.builder()
                .id(sourceAccountId)
                .balance(new BigDecimal("100.00"))
                .active(true)
                .build();

        TransferRequest request = new TransferRequest(
                sourceAccountId,
                targetAccountId,
                new BigDecimal("20.00"),
                "Transfer"
        );

        Mockito.when(accountRepository.findById(sourceAccountId))
                .thenReturn(Optional.of(sourceAccount));

        Mockito.when(accountRepository.findById(targetAccountId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> transactionService.transfer(request)
        );

        Assertions.assertEquals(
                "Target account not found",
                exception.getMessage()
        );

        Assertions.assertEquals(
                new BigDecimal("100.00"),
                sourceAccount.getBalance()
        );

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }

    // Prüfen Deposit - Konto existiert nicht
    @Test
    void shouldThrowExceptionWhenDepositAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();

        DepositRequest request = new DepositRequest(
                accountId,
                new BigDecimal("50.00"),
                "Deposit"
        );

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> transactionService.deposit(request)
        );

        Assertions.assertEquals(
                "Account not found",
                exception.getMessage()
        );

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }

    // Prüfen Withdrawal - Konto existiert nicht
    @Test
    void shouldThrowExceptionWhenWithdrawalAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();

        WithdrawalRequest request = new WithdrawalRequest(
                accountId,
                new BigDecimal("20.00"),
                "ATM withdrawal"
        );

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> transactionService.withdraw(request)
        );

        Assertions.assertEquals(
                "Account not found",
                exception.getMessage()
        );

        Mockito.verify(
                transactionRepository,
                Mockito.never()
        ).save(Mockito.any(Transaction.class));
    }
}
