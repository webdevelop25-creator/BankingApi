package com.marina.bankingapi.transaction.repository;
import com.marina.bankingapi.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySourceAccountIdOrTargetAccountId(UUID sourceAccountId, UUID targetAccountId);
}
