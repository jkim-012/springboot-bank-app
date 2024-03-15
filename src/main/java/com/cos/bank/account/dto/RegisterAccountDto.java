package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterAccountDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotBlank(message = "Password is a required field.")
        @Size(max = 4, message = "Password should be exactly 4 numbers.")
        @Pattern(regexp = "^[0-9]{4}$", message = "Password should consist of exactly 4 numbers.")
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{

        private Long id;
        private Long number;
        private Long balance;

        public static Response of(Account account) {
            return Response.builder()
                    .id(account.getId())
                    .number(account.getNumber())
                    .balance(account.getBalance())
                    .build();
        }
    }
}
