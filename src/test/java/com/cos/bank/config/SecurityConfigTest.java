package com.cos.bank.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityConfigTest {

    @Autowired
    private MockMvc mvc;

    @Disabled
    @Test
    public void authentication_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/random"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int status = resultActions.andReturn().getResponse().getStatus();

        System.out.println("test: " + responseBody);
        System.out.println("test: " + status);

        // then
        assertThat(status).isEqualTo(401);
    }
}
