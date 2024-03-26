package com.cos.bank.config.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwtValueObject {

    @Value("${jwt.secret}")
    private String secretKey;

    public static final int EXPIRATION_TIME = 1000 * 60 * 60 *24 * 7; // mil * sec * min * hour * day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

    public String getSecretKey() {
        return secretKey;
    }


}
