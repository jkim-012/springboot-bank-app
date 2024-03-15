package com.cos.bank.user.service.impl;

import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.dto.JoinDto;
import com.cos.bank.user.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest extends DummyObject {

    @InjectMocks
    private UserServiceImpl userServiceimpl;
    @Mock
    private UserRepository userRepository;
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void join_test_success() throws Exception{
        // given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("ssar");
        request.setPassword("1234");
        request.setEmail("ssar@email.com");
        request.setFirstName("first");
        request.setLastName("last");

        // stub1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty()); // no duplication -so we can process join

        // stub2
        User newUser = newMockUser(1L, "ssar", "first", "last");
        when(userRepository.save(any())).thenReturn(newUser);

        // when
        JoinDto.Response response = userServiceimpl.join(request);
        System.out.println("test result: " + response.toString());

        // then
        assertThat(request.getEmail()).isEqualTo(response.getEmail());
    }


    @Disabled
    @Test
    public void join_test_fail() throws Exception{
        // given
        JoinDto.Request request = new JoinDto.Request();
        request.setUsername("ssar");
        request.setPassword("1234");
        request.setEmail("ssar@email.com");
        request.setFirstName("first");
        request.setLastName("last");

        // stub1
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User())); //user exists

        // stub2
        User newUser = User.builder()
                .id(1L)
                .username("ssar2")
                .password("1234")
                .email("ssar@email.com")
                .firstName("first")
                .lastName("last")
                .role(Role.CUSTOMER)
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any())).thenReturn(newUser);

        // when
        JoinDto.Response response = userServiceimpl.join(request);
        System.out.println("test result: " + response.toString());

        // then
    }

}