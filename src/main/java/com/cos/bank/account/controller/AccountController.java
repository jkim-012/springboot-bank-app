package com.cos.bank.account.controller;


import com.cos.bank.account.dto.*;
import com.cos.bank.account.service.AccountService;
import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    // creating account API
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(
            @RequestBody @Valid RegisterAccountDto.Request request,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        RegisterAccountDto.Response response = accountService.createAccount(request, loginUser.getUser().getId());
        return ResponseEntity.ok()
                .body(new ResponseDto<>(1, "Account register succeeded.", response));
    }

    // reading user's account details
    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<?> getAccount(

            @PathVariable Long accountId,
            @AuthenticationPrincipal LoginUser loginUser){

        AccountDetailDto.Response response = accountService.getAccount(accountId, loginUser.getUser().getId());
        return ResponseEntity.ok()
                .body(new ResponseDto<>(1, "User account retrieved successfully.", response));
    }


    // reading user's account list API
    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccountsByUser(
            @AuthenticationPrincipal LoginUser loginUser){

        AccountListDto accountListDto = accountService.getAllAccounts(loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "User accounts retrieved successfully.", accountListDto), HttpStatus.OK);
    }

    // deleting account API
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal LoginUser loginUser){

        accountService.deleteAccount(accountId, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Account deletion succeeded.", null), HttpStatus.OK);
    }

    // ATM deposit API
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @RequestBody @Valid AccountDepositDto.Request request,
            BindingResult bindingResult){

        AccountDepositDto.Response response = accountService.deposit(request);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Deposit succeeded.", response), HttpStatus.OK);
    }

    // ATM withdrawal API
    @PostMapping("/account/withdrawal")
    public ResponseEntity<?> withdraw(
            @RequestBody @Valid AccountWithdrawDto.Request request,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser){

        AccountWithdrawDto.Response response = accountService.withdraw(request, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1,"Withdrawal succeeded.", response),HttpStatus.OK);
    }

    // transferring API
    @PostMapping("/account/transfer")
    public ResponseEntity<?> transfer(
            @RequestBody @Valid AccountTransferDto.Request request,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser){

        AccountTransferDto.Response response = accountService.transfer(request, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1,"Transfer succeeded.", response),HttpStatus.OK);
    }

}
