package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Long id;
    private Long number;
    private Long balance;

    public static AccountDto of(Account account){
        return AccountDto.builder()
                .id(account.getId())
                .number(account.getNumber())
                .balance(account.getBalance())
                .build();
    }
}
