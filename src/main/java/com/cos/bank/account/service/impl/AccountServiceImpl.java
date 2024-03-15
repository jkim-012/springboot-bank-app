package com.cos.bank.account.service.impl;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.AccountListDto;
import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.account.service.AccountService;
import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.handler.exception.CustomForbiddenException;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
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
                .balance(10L) // minimum $10
                .user(user)
                .build();

        accountRepository.save(account);
        return RegisterAccountDto.Response.of(account);
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
                .orElseThrow(()-> new CustomApiException("Account not found."));
        // check authority
        if (!account.getUser().getId().equals(userId)){
            throw new CustomForbiddenException("Unauthorized: You do not have permission to delete this account");
        }
        accountRepository.delete(account);
    }

    private Long generateAccountNumber() {
        Random random = new Random();
        long number = 1000000000L + random.nextLong() % 9000000000L;
        return Math.abs(number); // number is always positive
    }

    private User getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException("User not found."));
        return user;
    }
}
