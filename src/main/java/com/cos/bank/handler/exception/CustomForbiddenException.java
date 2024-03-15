package com.cos.bank.handler.exception;

public class CustomForbiddenException extends RuntimeException{
    public CustomForbiddenException(String message) {
        super(message);
    }
}
