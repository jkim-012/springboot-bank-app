package com.cos.bank.handler.exception;

public class CustomApiException extends RuntimeException{
    public CustomApiException(String message) {
        super(message);
    }
}
