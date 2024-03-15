package com.cos.bank.config.jwt;

import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void create_test() throws Exception {
        //given
        User user = User.builder()
                .id(1L)
                .role(Role.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);

        //when
        String token = JwtService.createJwtToken(loginUser);
        System.out.println(token);

        //then
        assertTrue(token.startsWith(JwtValueObject.TOKEN_PREFIX));
    }

    @Test
    void test() {
        //given
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTcxMTA1MzQ2NX0._gYd3yD_r7n5LWsx9zmJ6rL-u5GA-RKbOF-azxwGFMtfsPWw0ZjwAeZvS5eZjDHRlH3CScblYurSeKLAyXezCw";

        //when
        LoginUser loginUser = JwtService.verifyJwtToken(token);
        System.out.println(loginUser.getUser().getId());
        System.out.println(loginUser.getUser().getRole());

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(Role.CUSTOMER);

    }

}