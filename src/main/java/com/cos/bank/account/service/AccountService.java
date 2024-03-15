package com.cos.bank.account.service;

import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.user.dto.JoinDto;

public interface AccountService {

    RegisterAccountDto.Response createAccount(RegisterAccountDto.Request request, Long userId);
}
