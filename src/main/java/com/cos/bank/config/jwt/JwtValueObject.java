package com.cos.bank.config.jwt;

public class JwtValueObject {

    public static final String SECRET_KEY = "secretkey";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 *24 * 7; // mil * sec * min * hour * day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";



}
