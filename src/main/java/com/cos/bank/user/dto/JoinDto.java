package com.cos.bank.user.dto;

import com.cos.bank.user.domain.Role;
import com.cos.bank.user.domain.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class JoinDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "Username is a required field.")
        @Size(min = 4, max = 20, message = "Username should be between 4 and 20 characters.")
        @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$",
                message = "Username can contain any letters from a to z and any numbers from 0 through 9.")
        private String username;

        @NotBlank(message = "Password is a required field.")
        @Size(min = 4, max = 20, message = "Password should be between 4 and 20 characters.")
        private String password;

        @Email(message = "Please enter a valid email address.")
        @NotBlank(message = "Email is a required field.")
        private String email;

        @NotBlank(message = "First name is a required field.")
        private String firstName;

        @NotBlank(message = "Last name is a required field.")
        private String lastName;

    }

    @ToString
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Role role;

        public static Response of(User user) {
            return Response.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole())
                    .build();
        }

    }
}
