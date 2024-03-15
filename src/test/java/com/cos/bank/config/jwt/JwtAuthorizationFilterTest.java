package com.cos.bank.config.jwt;

import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void customer_authorization_success_test() throws Exception {
        //given
        // 1. need token - to get token, LoginUser needed
        User user = User.builder()
                .id(1L)
                .role(Role.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);

        // 2. get token
        String token = JwtService.createJwtToken(loginUser);

        //when
        ResultActions resultActions =
                mockMvc.perform(post("/api/s/test").header(JwtValueObject.HEADER, token));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void customer_authorization_fail_test() throws Exception {
        //given
        // 1. need token - to get token, LoginUser needed
        User user = User.builder()
                .id(1L)
                .role(Role.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);

        // 2. get token
        String token = JwtService.createJwtToken(loginUser);

        //when
        // no header (no token)
        ResultActions resultActions =
                mockMvc.perform(post("/api/s/test"));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void admin_authorization_success_test() throws Exception {
        //given
        // 1. need token - to get token, LoginUser needed
        User user = User.builder()
                .id(1L)
                .role(Role.ADMIN)
                .build();

        LoginUser loginUser = new LoginUser(user);

        // 2. get token
        String token = JwtService.createJwtToken(loginUser);

        //when
        ResultActions resultActions =
                mockMvc.perform(post("/api/admin/test").header(JwtValueObject.HEADER, token));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void admin_authorization_fail_test() throws Exception {
        //given
        // 1. need token - to get token, LoginUser needed
        User user = User.builder()
                .id(1L)
                .role(Role.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);

        // 2. get token
        String token = JwtService.createJwtToken(loginUser);

        //when
        // role : customer
        ResultActions resultActions =
                mockMvc.perform(post("/api/admin/test").header(JwtValueObject.HEADER, token));

        //then
        resultActions.andExpect(status().isForbidden());
    }


}