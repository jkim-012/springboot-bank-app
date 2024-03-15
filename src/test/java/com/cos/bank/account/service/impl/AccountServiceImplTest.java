package com.cos.bank.account.service.impl;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.AccountListDto;
import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
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

        // stub 1 - mocking user repository to return a user (user exists -> can create an account)
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
        // stub 1 - mocking user repository to return a user (user exists -> can create an account)
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return a list of accounts for the user
        List<Account> accounts = new ArrayList<>();
        accounts.add(newMockAccount(1L, 1234567891L, 10L, user));
        accounts.add(newMockAccount(2L, 1234567892L, 20L, user));

        // stub 3 - mocking
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

    @Disabled
    @Test
    void get_accounts_by_user_empty_test() throws Exception {
        // given
        Long userId = 1L;
        // stub 1 - mocking user repository to return a user (user exists -> can create an account)
        User user = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub 2 - mocking account repository to return a list of accounts for the user
        List<Account> accounts = new ArrayList<>();
        // stub 3 - mocking
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

}