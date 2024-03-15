package com.cos.bank.account.controller;

import com.cos.bank.account.dto.RegisterAccountDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setUp() {
        User user = userRepository.save(newUser("ssar", "first", "last")); // create user
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

}