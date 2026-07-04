package com.marina.bankingapi.account.repository;
import com.marina.bankingapi.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>{
    List<Account> findByUserId (UUID userId);
    boolean existsByIban (String iban);
}
