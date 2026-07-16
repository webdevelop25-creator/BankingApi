package com.marina.bankingapi.account.service;

import org.junit.jupiter.api.Assertions;
import com.marina.bankingapi.account.repository.AccountRepository;
import com.marina.bankingapi.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.marina.bankingapi.account.dto.AccountResponse;
import com.marina.bankingapi.account.dto.CreateAccountRequest;
import com.marina.bankingapi.account.entity.Account;
import com.marina.bankingapi.account.enums.AccountType;
import com.marina.bankingapi.auth.entity.User;


import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

class AccountServiceImplTest {
    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository = Mockito.mock(AccountRepository.class);
        userRepository = Mockito.mock(UserRepository.class);

        accountService = new AccountServiceImpl(
                accountRepository,
                userRepository
        );
    }

    // Prüfen Hauptanwendungsfall meines AccountService
    @Test
    void shouldCreateAccountSuccessfully() {
        // Arrange
        UUID userId = UUID.randomUUID();

        User user = User.builder()
                .id(userId)
                .email("marina@test.de")
                .build();

        CreateAccountRequest request = new CreateAccountRequest(
                userId,
                AccountType.CHECKING
        );

        Account savedAccount = Account.builder()
                .id(UUID.randomUUID())
                .iban("DE123456789")
                .balance(BigDecimal.ZERO)
                .accountType(AccountType.CHECKING)
                .active(true)
                .user(user)
                .build();

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        Mockito.when(accountRepository.save(Mockito.any(Account.class)))
                .thenReturn(savedAccount);

        // Act
        AccountResponse response = accountService.createAccount(request);

        // Assert
        Assertions.assertNotNull(response);

        Assertions.assertEquals(
                savedAccount.getId(),
                response.id()
        );

        Assertions.assertEquals(
                savedAccount.getIban(),
                response.iban()
        );

        Assertions.assertEquals(
                BigDecimal.ZERO,
                response.balance()
        );

        Assertions.assertEquals(
                AccountType.CHECKING,
                response.accountType()
        );

        Assertions.assertTrue(response.active());

        Mockito.verify(accountRepository)
                .save(Mockito.any(Account.class));

    }

    // Prüfen Fall Benutzer  nicht existiert (Ohne Benutzer darf kein Konto erstellt werden)
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        CreateAccountRequest request = new CreateAccountRequest(
                userId,
                AccountType.CHECKING
        );

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> accountService.createAccount(request)
        );

        Assertions.assertEquals(
                "User not found",
                exception.getMessage()
        );

        Mockito.verify(
                accountRepository,
                Mockito.never()
        ).save(Mockito.any(Account.class));
    }

    // Prüfen Konto gefunden
    @Test
    void shouldReturnAccountById() {
        UUID accountId = UUID.randomUUID();

        Account account = Account.builder()
                .id(accountId)
                .iban("DE123456789")
                .balance(new BigDecimal("100.00"))
                .accountType(AccountType.CHECKING)
                .active(true)
                .build();

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        AccountResponse response = accountService.getAccountById(accountId);

        Assertions.assertNotNull(response);

        Assertions.assertEquals(
                account.getId(),
                response.id()
        );

        Assertions.assertEquals(
                account.getIban(),
                response.iban()
        );

        Assertions.assertEquals(
                account.getBalance(),
                response.balance()
        );

        Assertions.assertEquals(
                account.getAccountType(),
                response.accountType()
        );

        Assertions.assertTrue(response.active());
    }

    //Prüfen Account nicht gefunden
    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();

        Mockito.when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> accountService.getAccountById(accountId)
        );

        Assertions.assertEquals(
                "Account not found",
                exception.getMessage()
        );
    }
}