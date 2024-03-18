package com.cos.bank.account.controller;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.AccountDepositDto;
import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        // make a user and their account
        User user1 = userRepository.save(newUser("ssar", "first", "last"));
        Account account1 = accountRepository.save(newAccount(1234567891L, user1));

        // make another user and their account to test delete api
        User user2 = userRepository.save(newUser("ssar2", "first", "last"));
        Account account2 = accountRepository.save(newAccount(1234567892L, user2));

        // clear persistence context to make the similar dev environment
        entityManager.clear();
    }

    //@WithUserDetails : execute before save_account_test,
    // search "ssar" in db and use it for authentication (currently login user info)
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void save_account_success_test() throws Exception {
        // given
        RegisterAccountDto.Request request = RegisterAccountDto.Request.builder()
                .password("1234")
                .build();

        String requestBody = om.writeValueAsString(request);
        System.out.println("test result: " + requestBody);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void get_accounts_success_test() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mockMvc.perform(get("/api/accounts").contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody); // will return empty list

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void delete_account_success_test() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/api/accounts/1"));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody);

        // then
        assertThrows(CustomApiException.class, () -> accountRepository.findById(1L).orElseThrow(
                () -> new CustomApiException("Account not found.")));
    }

    // logged in user : ssar2 (user2)
    @WithUserDetails(value = "ssar2", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void delete_account_fail_test() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/api/accounts/1"));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody);

        // then
        // no permission to delete because logged user (ssar2) is not the account 1 owner
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    void deposit_success_test() throws Exception {
        // given
        AccountDepositDto.Request request = AccountDepositDto.Request.builder()
                .depositAccountNumber(1234567891L) //ssar1's account number
                .amount(1000.0)
                .transactionType(TransactionType.DEPOSIT)
                .phone("1234567890")
                .build();

        String requestBody = om.writeValueAsString(request);
        System.out.println("test result: " + requestBody);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void deposit_fail_test() throws Exception {
        // given
        AccountDepositDto.Request request = AccountDepositDto.Request.builder()
                .depositAccountNumber(1234567894L) // this account number doesn't exist
                .amount(1000.0)
                .transactionType(TransactionType.DEPOSIT)
                .phone("1234567890")
                .build();

        String requestBody = om.writeValueAsString(request);
        System.out.println("test result: " + requestBody);

        // when
        ResultActions resultActions =
                mockMvc.perform(post("/api/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody);

        // then
        // fail due to Account not found exception (account number error)
        resultActions.andExpect(status().isBadRequest());
    }


}