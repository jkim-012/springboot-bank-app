package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.transaction.dto.TransactionDto;
import lombok.*;

import javax.validation.constraints.*;


public class AccountDepositDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotNull(message = "Deposit account number is a required field.")
        private Long depositAccountNumber;

        @NotNull(message = "Deposit amount is a required field.")
        private Double amount;

        @NotNull(message = "Transaction type is a required field.")
        private TransactionType transactionType;

        @NotBlank(message = "Phone number is a required field.")
        @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should consist of exactly 10 numbers.")
        private String phone;

        private String memo;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        private Long depositAccountId;
        private Long depositAccountNumber;
        private TransactionDto transactionDto;

        public static Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .depositAccountId(account.getId())
                    .depositAccountNumber(account.getNumber())
                    .transactionDto(TransactionDto.of(transaction))
                    .build();
        }
    }
}
