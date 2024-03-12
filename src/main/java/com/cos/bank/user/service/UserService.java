package com.cos.bank.user.service;

import com.cos.bank.user.dto.JoinDto;

public interface UserService {

    JoinDto.Response join(JoinDto.Request request);
}
