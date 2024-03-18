package com.cos.bank.config.jwt;

import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    @Test
    void token_create_test() throws Exception {
        // given

        // when
        String jwtToken = createToken();
        System.out.println("test result : " + jwtToken);

        // then
        assertTrue(jwtToken.startsWith(JwtValueObject.TOKEN_PREFIX));
    }

    @Test
    void token_verify_test() {
        //given
        String token = createToken();
        // only get token from authorization value
        String extractedToken = token.replace(JwtValueObject.TOKEN_PREFIX, "");

        //when
        LoginUser loginUser = JwtService.verifyJwtToken(extractedToken);
        System.out.println(loginUser.getUser().getId());
        System.out.println(loginUser.getUser().getRole());

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(Role.CUSTOMER);

    }

    private String createToken() {
        User user = User.builder()
                .id(1L)
                .role(Role.CUSTOMER)
                .build();

        LoginUser loginUser = new LoginUser(user);

        return JwtService.createJwtToken(loginUser);
    }

}