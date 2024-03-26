package com.cos.bank.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class JwtService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // get the secret key from JwtValueObject
    private static final String secretKey = new JwtValueObject().getSecretKey();

    public static String createJwtToken(LoginUser loginUser) {


        String jwtToken = JWT.create()
                .withSubject("bank")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtValueObject.EXPIRATION_TIME))
                .withClaim("id", loginUser.getUser().getId())
                .withClaim("role", loginUser.getUser().getRole().toString())
                .sign(Algorithm.HMAC512(secretKey));

        return JwtValueObject.TOKEN_PREFIX + jwtToken;
    }

    public static LoginUser verifyJwtToken(String token) {
        // decode the token
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);

        // get id, role, and expiration
        Long id = decodedJWT.getClaim("id").asLong();
        String role = decodedJWT.getClaim("role").asString();
        Date expirationAt = decodedJWT.getExpiresAt();

        // make user with extracted id and role
        User user = User.builder()
                .id(id)
                .role(Role.valueOf(role))
                .build();
        // generate loginUser (userDetails)
        LoginUser loginUser = new LoginUser(user);
        return loginUser;
    }

}
