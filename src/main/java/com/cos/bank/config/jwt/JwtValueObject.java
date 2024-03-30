package com.cos.bank.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValueObject {
    public static String secretKey;

    @Value("${jwt.key}")
    public void setSecretKey(String secretKey){
        JwtValueObject.secretKey = secretKey;
    }
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 *24 * 7; // mil * sec * min * hour * day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
