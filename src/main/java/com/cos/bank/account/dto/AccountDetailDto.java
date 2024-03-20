package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import lombok.*;

public class AccountDetailDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{

        private Long id;
        private Long number;
        private Double balance;

        public static AccountDetailDto.Response of(Account account){
            return Response.builder()
                    .id(account.getId())
                    .number(account.getNumber())
                    .balance(account.getBalance())
                    .build();
        }
    }
}
