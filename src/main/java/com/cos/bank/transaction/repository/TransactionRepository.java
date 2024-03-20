package com.cos.bank.transaction.repository;

import com.cos.bank.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, Dao{
}
