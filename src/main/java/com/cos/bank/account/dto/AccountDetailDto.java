package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import lombok.*;

import java.math.BigDecimal;

public class AccountDetailDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{

        private Long id;
        private Long number;
        private BigDecimal balance;

        public static AccountDetailDto.Response of(Account account){
            return Response.builder()
                    .id(account.getId())
                    .number(account.getNumber())
                    .balance(account.getBalance())
                    .build();
        }
    }
}
