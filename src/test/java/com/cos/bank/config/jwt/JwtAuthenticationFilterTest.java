package com.cos.bank.config.jwt;

import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.dto.LoginDto;
import com.cos.bank.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired
    private ObjectMapper om; //object -> JSON

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        User user = userRepository.save(newUser("ssar", "first", "last"));
    }

    @Test
    void successfulAuthentication_test() throws Exception {
        //given
        LoginDto.Request loginDto = new LoginDto.Request();
        loginDto.setUsername("ssar");
        loginDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginDto); //object into a JSON string
        System.out.println(requestBody);

        //when
        ResultActions resultActions =
                mockMvc.perform(post("/api/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtValueObject.HEADER);
        System.out.println("test result: " + responseBody);
        System.out.println("test result: " + jwtToken);

        //then
        resultActions.andExpect(status().isOk()); //status = 200
        assertNotNull(jwtToken); // token exists
        assertTrue(jwtToken.startsWith(JwtValueObject.TOKEN_PREFIX)); //token starts with "Bearer "
        resultActions.andExpect(jsonPath("$.data.username").value("ssar")); //response has dto and dto has user info

    }

    @Test
    void unsuccessfulAuthentication_test() throws Exception {

        //given
        LoginDto.Request loginDto = new LoginDto.Request();
        loginDto.setUsername("");
        loginDto.setPassword("1234");

        String requestBody = om.writeValueAsString(loginDto); //object into a JSON string
        System.out.println(requestBody);

        //when
        ResultActions resultActions =
                mockMvc.perform(post("/api/login")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON));

        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtValueObject.HEADER);
        System.out.println("test result: " + responseBody);
        System.out.println("test result: " + jwtToken);

        //then
        resultActions.andExpect(status().isBadRequest());

    }

}