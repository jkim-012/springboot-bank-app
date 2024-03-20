package com.cos.bank.config.dummy;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected User newUser(String username, String firstName, String lastName) {

        String encodedPw = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encodedPw)
                .email(username + "@email.com")
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.CUSTOMER)
                .build();
    }

    protected User newMockUser(Long id, String username, String firstName, String lastName) {

        String encodedPw = passwordEncoder.encode("1234");

        return User.builder()
                .id(id)
                .username(username)
                .password(encodedPw)
                .email(username + "@email.com")
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.CUSTOMER)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

    }

    protected Account newAccount(Long number, User user) {

        String encodedPw = passwordEncoder.encode("1234");

        return Account.builder()
                .number(number)
                .password(encodedPw)
                .balance(100.0)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Double balance, User user) {
        String encodedPw = passwordEncoder.encode("1234");

        return Account.builder()
                .id(id)
                .number(number)
                .password(encodedPw)
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account account){
        account.deposit(10.0);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(null)
                .depositAccount(account)
                .amount(10.0)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(String.valueOf(account.getNumber()))
                .phone("1234567890")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockWithdrawTransaction(Long id, Account account){
        account.withdraw( 10.0);
        return Transaction.builder()
                .id(id)
                .withdrawAccount(account)
                .depositAccount(null)
                .amount(10.0)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .transactionType(TransactionType.WITHDRAW)
                .sender(String.valueOf(account.getNumber()))
                .receiver(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(10.0);

        // Repository Test - dirty checking (o)
        // Controller Test - dirty checking (x)
        if (accountRepository != null) {
            accountRepository.save(account);
        }
        return Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .amount(10.0)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .transactionType(TransactionType.WITHDRAW)
                .sender(String.valueOf(account.getNumber()))
                .receiver(null)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(10.0);

        // Repository Test - dirty checking (o)
        // Controller Test - dirty checking (x)
        if (accountRepository != null) {
            accountRepository.save(account);
        }

        return Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .amount(10.0)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(String.valueOf(account.getNumber()))
                .phone("1234567890")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
                                                 AccountRepository accountRepository) {
        withdrawAccount.withdraw(10.0);
        depositAccount.deposit(10.0);

        // Repository Test - dirty checking (o)
        // Controller Test - dirty checking (x)
        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        return Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(10.0)
                .transactionType(TransactionType.TRANSFER)
                .sender(String.valueOf(withdrawAccount.getNumber()))
                .receiver(String.valueOf(depositAccount.getNumber()))
                .build();
    }



}
