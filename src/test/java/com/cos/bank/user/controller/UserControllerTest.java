package com.cos.bank.user.controller;

import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.dto.JoinDto;
import com.cos.bank.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest extends DummyObject {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @BeforeEach
    void setUp() throws Exception {
        User user = userRepository.save(newUser("ssar", "first", "last"));
    }

    @Test
    public void join_success_test() throws Exception {
        //given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("ssarrrrr");
        request.setPassword("1234");
        request.setEmail("ssar@email.com");
        request.setFirstName("first");
        request.setLastName("last");

        String requestBody = om.writeValueAsString(request);
        System.out.println("테스트 : " + requestBody);

        // when
        ResultActions resultActions = mockMvc
                .perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void join_fail_test() throws Exception {
        //given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("ssar"); // this username already exists in db
        request.setPassword("1234");
        request.setEmail("ssar@email.com");
        request.setFirstName("first");
        request.setLastName("last");

        String requestBody = om.writeValueAsString(request);

        //when
        ResultActions resultActions =
                mockMvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("test: " + responseBody);

        //then
        resultActions.andExpect(status().isBadRequest()); //because of the username duplication check
    }


}