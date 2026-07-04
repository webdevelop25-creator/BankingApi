package com.marina.bankingapi.account.service;
import com.marina.bankingapi.account.dto.AccountResponse;
import com.marina.bankingapi.account.dto.CreateAccountRequest;
import com.marina.bankingapi.account.repository.AccountRepository;
import com.marina.bankingapi.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.marina.bankingapi.account.entity.Account;
import com.marina.bankingapi.auth.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {

        // Hier kommt gleich unsere Logik hinein

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = Account.builder()
                .iban(generateIban())
                .balance(BigDecimal.ZERO)
                .accountType(request.accountType())
                .active(true)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(
                savedAccount.getId(),
                savedAccount.getIban(),
                savedAccount.getBalance(),
                savedAccount.getAccountType(),
                savedAccount.isActive()
        );
    }
    private String generateIban() {
        return "DE" + System.currentTimeMillis();
    }
}
