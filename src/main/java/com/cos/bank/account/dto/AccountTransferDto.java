package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.transaction.dto.TransactionDto;
import lombok.*;

import javax.validation.constraints.*;

public class AccountTransferDto {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request{

        @NotNull(message = "Transfer account number is a required field.")
        @Digits(integer = 10, fraction = 0, message = "Transfer account number must be exactly 10 digits.")
        private Long withdrawAccountNumber;

        @NotNull(message = "Transfer account number is a required field.")
        @Digits(integer = 10, fraction = 0, message = "Transfer account number must be exactly 10 digits.")
        private Long depositAccountNumber;

        @NotBlank(message = "Password is a required field.")
        @Size(min = 4, max = 4, message = "Password should be exactly 4 numbers.")
        @Pattern(regexp = "^[0-9]{4}$", message = "Password should consist of exactly 4 numbers.")
        private String withdrawAccountPw;

        @NotNull(message = "Transfer amount is a required field.")
        private Double amount;

        @NotNull(message = "Transaction type is a required field.")
        private TransactionType transactionType; // TRANSFER

        private String memo;

    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response{
        // response for a withdrawal account's owner
        private Long withdrawAccountId;
        private Long withdrawAccountNumber;
        private Double withdrawAccountBalance;
        private TransactionDto transactionDto;

        public static AccountTransferDto.Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .withdrawAccountId(account.getId())
                    .withdrawAccountNumber(account.getNumber())
                    .withdrawAccountBalance(account.getBalance())
                    .transactionDto(TransactionDto.of(transaction))
                    .build();
        }
    }
}
