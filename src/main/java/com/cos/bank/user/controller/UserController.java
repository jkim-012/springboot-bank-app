package com.cos.bank.user.controller;

import com.cos.bank.util.ResponseDto;
import com.cos.bank.user.dto.JoinDto;
import com.cos.bank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;

    // API endpoint for user registration with the bank
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinDto.Request request, BindingResult bindingResult){
        JoinDto.Response response = userService.join(request);
        return ResponseEntity.ok().body(new ResponseDto<>(1, "User join succeeded", response));
    }

}
