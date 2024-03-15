package com.cos.bank.account.controller;


import com.cos.bank.account.dto.RegisterAccountDto;
import com.cos.bank.account.service.AccountService;
import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AccountController {

    private final AccountService accountService;

    // API for creating an account
    @PostMapping("/account")
    public ResponseEntity<?> createAccount(
            @RequestBody @Valid RegisterAccountDto.Request request,
            BindingResult bindingResult,
            @AuthenticationPrincipal LoginUser loginUser) {

        RegisterAccountDto.Response response = accountService.createAccount(request, loginUser.getUser().getId());
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Account register succeeded.", response), HttpStatus.OK);
    }
}
