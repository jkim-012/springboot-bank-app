package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountListDto {

    private String firstName;
    private String lastName;
    private List<AccountDto> accountDtos;

    public static AccountListDto of(User user, List<Account> accounts) {
        return AccountListDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .accountDtos(accounts.stream().map(AccountDto::of).collect(Collectors.toList()))
                .build();
    }

}
