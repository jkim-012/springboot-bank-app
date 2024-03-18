package com.cos.bank.config.dummy;

import com.cos.bank.account.domain.Account;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String firstName, String lastName) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPw = passwordEncoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encodedPw)
                .email(username + "@email.com")
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.CUSTOMER)
                .build();
    }

    protected User newMockUser(Long id, String username, String firstName, String lastName) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPw = passwordEncoder.encode("1234");

        return User.builder()
                .id(id)
                .username(username)
                .password(encodedPw)
                .email(username + "@email.com")
                .firstName(firstName)
                .lastName(lastName)
                .role(Role.CUSTOMER)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

    }

    protected Account newAccount(Long number, User user) {
        return Account.builder()
                .number(number)
                .password("1234")
                .balance(10.0)
                .user(user)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Double balance, User user) {
        return Account.builder()
                .id(id)
                .number(number)
                .password("1234")
                .balance(balance)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
