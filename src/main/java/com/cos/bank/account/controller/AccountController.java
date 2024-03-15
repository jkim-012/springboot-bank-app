package com.cos.bank.account.controller;


import com.cos.bank.account.dto.AccountListDto;
import com.cos.bank.account.dto.RegisterAccountDto;
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

    // API for creating account
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(
            @RequestBody @Valid RegisterAccountDto.Request request,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        RegisterAccountDto.Response response = accountService.createAccount(request, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Account register succeeded.", response), HttpStatus.OK);
    }

    // API for reading user's account list
    @GetMapping("/accounts")
    public ResponseEntity<?> getAllAccountsByUser(
            @AuthenticationPrincipal LoginUser loginUser){
        AccountListDto accountListDto = accountService.getAllAccounts(loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "User accounts retrieved successfully.", accountListDto), HttpStatus.OK);
    }

    // API for deleting account
    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal LoginUser loginUser){
        accountService.deleteAccount(accountId, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Account deletion succeeded.", null), HttpStatus.OK);
    }

}
