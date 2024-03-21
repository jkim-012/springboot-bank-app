package com.cos.bank.account.service.impl;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.*;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.account.service.AccountService;
import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.handler.exception.CustomForbiddenException;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.transaction.repository.TransactionRepository;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterAccountDto.Response createAccount(RegisterAccountDto.Request request, Long userId) {
        // check if user exists
        User user = getUser(userId);
        // create account number
        Long accountNumber;
        boolean existsInDb = false;
        do {
            accountNumber = generateAccountNumber(); //generate random 10 digits
            System.out.println(accountNumber);
            if (accountRepository.findByNumber(accountNumber).isPresent()) {
                existsInDb = true; // if the same number exits in db
            }
        } while (existsInDb);

        // create account
        Account account = Account.builder()
                .number(accountNumber)
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(BigDecimal.valueOf(10.0)) // minimum $10
                .user(user)
                .build();

        accountRepository.save(account);
        return RegisterAccountDto.Response.of(account);
    }
    @Override
    public AccountDetailDto.Response getAccount(Long accountId, Long userId) {
        // check if user exists
        User user = getUser(userId);
        // get account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(()-> new CustomApiException("Account not found."));
        return AccountDetailDto.Response.of(account);
    }

    @Override
    public AccountListDto getAllAccounts(Long userId) {
        // check if user exists
        User user = getUser(userId);
        // get all accounts by user
        List<Account> accounts = accountRepository.findByUser_Id(userId);
        return AccountListDto.of(user, accounts);
    }


    @Transactional
    @Override
    public void deleteAccount(Long accountId, Long userId) {
        // check if user exists
        User user = getUser(userId);
        // find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomApiException("Account not found."));
        // check authority
        if (!account.getUser().getId().equals(userId)) {
            throw new CustomForbiddenException("Unauthorized: You do not have permission to delete this account");
        }
        accountRepository.delete(account);
    }

    @Transactional
    @Override
    public AccountDepositDto.Response deposit(AccountDepositDto.Request request) {
        // check deposit amount - should be greater than 0
        validateAmount(request.getAmount());
        // find account
        Account account = accountRepository.findByNumber(request.getDepositAccountNumber())
                .orElseThrow(() -> new CustomApiException("Account not found."));

        // deposit into the found account
        account.deposit(request.getAmount());

        // create transaction info(history)
        Transaction transaction = Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .amount(request.getAmount())
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .transactionType(TransactionType.DEPOSIT)
                .sender("ATM")
                .receiver(String.valueOf(request.getDepositAccountNumber()))
                .phone(request.getPhone())
                .build();

        transactionRepository.save(transaction);
        return AccountDepositDto.Response.of(account, transaction);
    }


    @Transactional
    @Override
    public AccountWithdrawDto.Response withdraw(AccountWithdrawDto.Request request, Long userId) {
        // check withdrawal amount - should be greater than 0
        validateAmount(request.getAmount());
        // find account
        Account account = accountRepository.findByNumber(request.getWithdrawAccountNumber())
                .orElseThrow(() -> new CustomApiException("Account not found."));

        // check authority
        if (!account.getUser().getId().equals(userId)) {
            throw new CustomForbiddenException
                    ("Unauthorized: You do not have permission to withdraw from this account");
        }
        // verify password
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new CustomApiException("Incorrect account password.");
        }
        // current balance check (should have more than withdrawal amount)
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CustomApiException("Insufficient funds.");
        }
        // withdraw
        account.withdraw(request.getAmount());
        // create transaction info(history)
        Transaction transaction = Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .amount(request.getAmount())
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .transactionType(TransactionType.WITHDRAW)
                .sender(String.valueOf(request.getWithdrawAccountNumber()))
                .receiver("ATM")
                .build();

        transactionRepository.save(transaction);
        return AccountWithdrawDto.Response.of(account, transaction);
    }

    @Transactional
    @Override
    public AccountTransferDto.Response transfer(AccountTransferDto.Request request, Long userId) {
        // check accounts if both accounts are the same account
        if (request.getWithdrawAccountNumber().equals(request.getDepositAccountNumber())){
            throw new CustomApiException("You can't send money to the same account. " +
                    "Please choose a different account for the deposit.");
        }
        // check transfer amount - should be greater than 0
        validateAmount(request.getAmount());

        // check withdraw account number
        Account withdrawAccount = accountRepository.findByNumber(request.getWithdrawAccountNumber())
                .orElseThrow(()-> new CustomApiException("Account not found."));
        // check deposit account number
        Account depositAccount = accountRepository.findByNumber(request.getDepositAccountNumber())
                .orElseThrow(()-> new CustomApiException("Account not found."));
        // check authority
        if (!withdrawAccount.getUser().getId().equals(userId)) {
            throw new CustomForbiddenException("Unauthorized: You do not have permission to transfer from this account");
        }
        // current balance check (should have more than transfer amount)
        if (withdrawAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CustomApiException("Insufficient funds.");
        }
        // verify password
        if (!passwordEncoder.matches(request.getWithdrawAccountPw(), withdrawAccount.getPassword())) {
            throw new CustomApiException("Incorrect account password.");
        }
        // transfer
        withdrawAccount.withdraw(request.getAmount()); // withdraw
        depositAccount.deposit(request.getAmount()); // deposit

        // create transaction info(history)
        Transaction transaction = Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .amount(request.getAmount())
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .transactionType(TransactionType.TRANSFER)
                .sender(String.valueOf(request.getWithdrawAccountNumber()))
                .receiver(String.valueOf(request.getDepositAccountNumber()))
                .build();

        transactionRepository.save(transaction);
        return AccountTransferDto.Response.of(withdrawAccount, transaction);
    }

    private static void validateAmount(BigDecimal amount) {
        //check amount
        //Negative if the BigDecimal is less than zero.
        //Zero if the BigDecimal is equal to zero.
        //Positive if the BigDecimal is greater than zero.
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomApiException("You can't deposit 0 or less amount.");
        }
    }
    private Long generateAccountNumber() {
        Random random = new Random();
        long number = 1000000000L + random.nextInt(900000000); // generate a random number in the range [1000000000, 1899999999]
        return number;
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("User not found."));
        return user;
    }
}
