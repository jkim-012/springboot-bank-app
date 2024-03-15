package com.cos.bank.account.repository;

import com.cos.bank.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByNumber(Long accountNumber);
    List<Account> findByUser_Id(Long userId);
}
