package com.cos.bank.config.dummy;

import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    protected User newUser(String username, String firstName, String lastName) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPw = passwordEncoder.encode("1234");

        return User.builder()
                .id(1L)
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
}
