package com.cos.bank.account.service;

import com.cos.bank.account.dto.AccountListDto;
import com.cos.bank.account.dto.RegisterAccountDto;

public interface AccountService {

    RegisterAccountDto.Response createAccount(RegisterAccountDto.Request request, Long userId);
    AccountListDto getAllAccounts(Long userId);
}
