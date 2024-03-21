package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.util.CustomDataFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
        private BigDecimal amount;

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
        private BigDecimal accountBalance;
        private TransactionDto transactionDto;

        public static AccountWithdrawDto.Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .withdrawAccountId(account.getId())
                    .withdrawAccountNumber(account.getNumber())
                    .accountBalance(account.getBalance())
                    .transactionDto(AccountWithdrawDto.TransactionDto.of(transaction))
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionDto {

        private Long id;
        private BigDecimal amount;
        private TransactionType transactionType;
        private String sender;
        private String receiver;
        private String phone;
        private String memo;
        private String createdAt;
        private String updatedAt;

        @JsonIgnore
        private BigDecimal depositAccountBalance;
        @JsonIgnore
        private BigDecimal withdrawAccountBalance;

        public static AccountWithdrawDto.TransactionDto of(Transaction transaction) {
            return AccountWithdrawDto.TransactionDto.builder()
                    .id(transaction.getId())
                    .amount(transaction.getAmount())
                    .transactionType(transaction.getTransactionType())
                    .sender(transaction.getSender())
                    .receiver(transaction.getReceiver())
                    .phone(transaction.getPhone())
                    .memo(transaction.getMemo())
                    .createdAt(CustomDataFormatter.of(transaction.getCreatedAt())) // yyyy-MM-dd HH:mm:ss
                    .updatedAt(CustomDataFormatter.of(transaction.getUpdatedAt())) // yyyy-MM-dd HH:mm:ss
                    .depositAccountBalance(transaction.getDepositAccountBalance()) // only used to test
                    .withdrawAccountBalance(transaction.getWithdrawAccountBalance()) // only used to test
                    .build();
        }
    }
}
