package com.cos.bank.account.controller;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.account.service.impl.AccountServiceImpl;
import com.cos.bank.config.dummy.DummyObject;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
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

    @BeforeEach
    void setUp() {
        User user = userRepository.save(newUser("ssar", "first", "last")); // create user
        Account account = accountRepository.save(newAccount(1234567891L, user));
    }

    //execute before save_account_test, search "ssar" in db and use it for authentication
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void save_account_test() throws Exception {
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
    void get_accounts_test() throws Exception {
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
    void delete_account_test() throws Exception {
        // given
        // when
        ResultActions resultActions =
                mockMvc.perform(delete("/api/accounts/1").contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test result: " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }



}