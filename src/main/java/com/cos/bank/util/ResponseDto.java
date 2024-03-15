package com.cos.bank.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {

    private final Integer code;
    private final String message;
    private final T data;
}
