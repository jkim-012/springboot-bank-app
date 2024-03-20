package com.cos.bank.transaction.controller;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.repository.TransactionRepository;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest extends DummyObject {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        dataSetting();
        entityManager.clear();
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void find_transaction_list_test() throws Exception {
        // given
        Long accountId = 1L;
        String transactionType = "ALL";
        String page = "0";

        // when
        ResultActions resultActions =
                mockMvc.perform(get("/api/accounts/" + accountId + "/transactions")
                        .param("transactionType", transactionType).param("page", page));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test check: " + responseBody);

        // then
        resultActions.andExpect(jsonPath("$.data.transactionDtos[0].balance").value(90));
        resultActions.andExpect(jsonPath("$.data.transactionDtos[1].balance").value(80));
        resultActions.andExpect(jsonPath("$.data.transactionDtos[2].balance").value(70));
        resultActions.andExpect(jsonPath("$.data.transactionDtos[3].balance").value(80));

    }

    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "first", "last"));
        User cos = userRepository.save(newUser("cos", "first", "last"));
        User love = userRepository.save(newUser("love", "first", "last"));
        User admin = userRepository.save(newUser("admin", "first", "last"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111111111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(222222222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333333333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444444444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }

}