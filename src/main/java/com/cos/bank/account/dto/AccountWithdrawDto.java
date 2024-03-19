package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.transaction.dto.TransactionDto;
import lombok.*;

import javax.validation.constraints.*;

public class AccountWithdrawDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotNull(message = "Withdraw account number is a required field.")
        @Digits(integer = 10, fraction = 0, message = "Withdraw account number must be exactly 10 digits.")
        private Long withdrawAccountNumber;

        @NotBlank(message = "Password is a required field.")
        @Size(min = 4, max = 4, message = "Password should be exactly 4 numbers.")
        @Pattern(regexp = "^[0-9]{4}$", message = "Password should consist of exactly 4 numbers.")
        private String password;


        @NotNull(message = "Withdraw amount is a required field.")
        private Double amount;

        @NotNull(message = "Transaction type is a required field.")
        private TransactionType transactionType; // WITHDRAW

        private String memo;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long withdrawAccountId;
        private Long withdrawAccountNumber;
        private Double accountBalance;
        private TransactionDto transactionDto;

        public static AccountWithdrawDto.Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .withdrawAccountId(account.getId())
                    .withdrawAccountNumber(account.getNumber())
                    .accountBalance(account.getBalance())
                    .transactionDto(TransactionDto.of(transaction))
                    .build();
        }
    }
}
