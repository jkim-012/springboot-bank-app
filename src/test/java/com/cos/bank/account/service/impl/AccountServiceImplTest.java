package com.cos.bank.account.service.impl;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.AccountDepositDto;
import com.cos.bank.account.dto.AccountListDto;
import com.cos.bank.account.dto.AccountWithdrawDto;
import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.transaction.repository.TransactionRepository;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest extends DummyObject {

    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Spy
    private ObjectMapper om;
    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void create_account_success_test() throws JsonProcessingException {
        // given
        RegisterAccountDto.Request request = RegisterAccountDto.Request.builder()
                .password("1234")
                .build();

        Long userId = 1L;

        // stub 1 - mocking user repository to return a user
        // (user exists -> can create an account)
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return account number doesn't exist
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty()); //number doesn't exist

        // when
        RegisterAccountDto.Response response = accountService.createAccount(request, userId);
        String responseBody = om.writeValueAsString(response);
        System.out.println("test result: " + responseBody);

        // then
        verify(userRepository).findById(userId);
        verify(accountRepository).findByNumber(any());
        assertThat(response.getBalance()).isEqualTo(10L);

    }

    @Test
    void get_accounts_by_user_success_test() throws Exception {
        // given
        Long userId = 1L;
        // stub 1 - mocking user repository to return a user
        // (user exists -> can get accounts)
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return a list of accounts for the user
        List<Account> accounts = new ArrayList<>();
        accounts.add(newMockAccount(1L, 1234567891L, 10.0, user));
        accounts.add(newMockAccount(2L, 1234567892L, 20.0, user));
        when(accountRepository.findByUser_Id(userId)).thenReturn(accounts);

        // when
        AccountListDto accountListDto = accountService.getAllAccounts(userId);
        String responseBody = om.writeValueAsString(accountListDto);
        System.out.println("test result: " + responseBody);

        // then
        verify(userRepository).findById(userId);
        verify(accountRepository).findByUser_Id(userId);
        assertThat(accountListDto.getAccountDtos().size()).isEqualTo(2);
    }

    @Test
    void get_accounts_by_user_empty_test() throws Exception {
        // given
        Long userId = 1L;
        // stub 1 - mocking user repository to return a user
        // (user exists -> can get accounts)
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return a list of accounts for the user
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findByUser_Id(userId)).thenReturn(accounts); //empty list

        // when
        AccountListDto accountListDto = accountService.getAllAccounts(userId);
        String responseBody = om.writeValueAsString(accountListDto);
        System.out.println("test result: " + responseBody);

        // then
        verify(userRepository).findById(userId);
        verify(accountRepository).findByUser_Id(userId);
        assertThat(accountListDto.getAccountDtos().size()).isEqualTo(0);
    }

    @Test
    void delete_account_success_test() {
        // given
        Long userId = 1L;
        Long accountId = 1L;
        // stub 1 - mocking user repository to return user
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return account
        Account account = newMockAccount(1L, 1234567891L, 10.0, user);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        // when
        accountService.deleteAccount(accountId, userId);
        // then
        verify(userRepository).findById(userId);
        verify(accountRepository).findById(accountId);
        verify(accountRepository).delete(account);
    }

    @Test
    void delete_account_fail_test() {
        // given
        Long userId = 1L;
        Long accountId = 1L;
        // stub 1 - mocking user repository to return user
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // stub 2 - mocking account repository to return nothing ( account doesn't exits)
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(CustomApiException.class, () -> accountService.deleteAccount(accountId, userId));
    }

    @Test
    void deposit_success_test() {
        // given
        AccountDepositDto.Request request = AccountDepositDto.Request.builder()
                .depositAccountNumber(1234567891L) // this account number doesn't exist
                .amount(1000.0)
                .transactionType(TransactionType.DEPOSIT)
                .phone("1234567890")
                .build();

        // stub 1 - mocking account repository return the deposit account
        // create user and its account
        User user = newMockUser(1L, "ssar", "first", "last");
        Account account = newMockAccount(1L, 1234567891L, 10.0, user);
        when(accountRepository.findByNumber(request.getDepositAccountNumber())).thenReturn(Optional.of(account));

        // stub 2 - mocking transaction repository to save transaction
        Transaction transaction = newMockDepositTransaction(1L, newMockAccount(1L, 1234567891L, 10.0, user));
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when,
        AccountDepositDto.Response response = accountService.deposit(request);
        // check transaction balance
        System.out.println("test result: " + response.getTransactionDto().getDepositAccountBalance());
        System.out.println("test result: " + account.getBalance());

        // then
        assertThat(account.getBalance()).isEqualTo(1010.0);
        assertThat(response.getTransactionDto().getDepositAccountBalance()).isEqualTo(1010.0);
    }

    @Test
    void withdraw_success_test() {
        // given
        AccountWithdrawDto.Request request = AccountWithdrawDto.Request.builder()
                .amount(10.0)
                .withdrawAccountNumber(1234567891L)
                .password("1234")
                .transactionType(TransactionType.WITHDRAW)
                .build();

        // stub 1 - mocking account repository return the deposit account
        // create user and its account
        User user = newMockUser(1L, "ssar", "first", "last");
        Account account = newMockAccount(1L, 1234567891L, 100.0, user);
        when(accountRepository.findByNumber(request.getWithdrawAccountNumber())).thenReturn(Optional.of(account));

        // stub 2 - mocking transaction repository to save transaction
        Transaction transaction = newMockWithdrawTransaction(1L, newMockAccount(1L, 1234567891L, 100.0, user));
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        AccountWithdrawDto.Response response = accountService.withdraw(request, 1L);
        // check transaction balance
        System.out.println("test result: " + response.getTransactionDto().getWithdrawAccountBalance());
        System.out.println("test result: " + account.getBalance());

        // then
        assertThat(account.getBalance()).isEqualTo(90.0);
        assertThat(response.getTransactionDto().getWithdrawAccountBalance()).isEqualTo(90.0);

    }
}