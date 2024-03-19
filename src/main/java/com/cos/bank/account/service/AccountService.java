package com.cos.bank.account.service;

import com.cos.bank.account.dto.*;

public interface AccountService {

    RegisterAccountDto.Response createAccount(RegisterAccountDto.Request request, Long userId);
    AccountListDto getAllAccounts(Long userId);
    void deleteAccount(Long accountId, Long id);
    AccountDepositDto.Response deposit(AccountDepositDto.Request request);
    AccountWithdrawDto.Response withdraw(AccountWithdrawDto.Request request, Long userId);
    AccountTransferDto.Response transfer(AccountTransferDto.Request request, Long userId);
}
