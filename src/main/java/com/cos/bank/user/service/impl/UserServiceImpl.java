package com.cos.bank.user.service.impl;

import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.dto.JoinDto;
import com.cos.bank.user.repository.UserRepository;
import com.cos.bank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public JoinDto.Response join(JoinDto.Request request){
        // duplication check
        Optional<User> byUsername = userRepository.findByUsername(request.getUsername());
        // throw exception if user exists
        if(byUsername.isPresent()){
            throw new CustomApiException("Username is already exists. Please login with the existing account.");
        }
        // process join
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);

        return JoinDto.Response.of(user);
    }

}
