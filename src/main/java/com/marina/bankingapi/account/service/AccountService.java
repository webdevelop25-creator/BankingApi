package com.marina.bankingapi.account.service;
import com.marina.bankingapi.account.dto.AccountResponse;
import com.marina.bankingapi.account.dto.CreateAccountRequest;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    AccountResponse getAccountById(UUID accountId);
}
