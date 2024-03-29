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
        private BigDecimal amount;

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
        private BigDecimal withdrawAccountBalance;
        private TransactionDto transactionDto;

        public static AccountTransferDto.Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .withdrawAccountId(account.getId())
                    .withdrawAccountNumber(account.getNumber())
                    .withdrawAccountBalance(account.getBalance())
                    .transactionDto(AccountTransferDto.TransactionDto.of(transaction))
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

        public static AccountTransferDto.TransactionDto of(Transaction transaction) {
            return AccountTransferDto.TransactionDto.builder()
                    .id(transaction.getId())
                    .amount(transaction.getAmount())
                    .transactionType(transaction.getTransactionType())
                    .sender(transaction.getSender())
                    .receiver(transaction.getReceiver())
                    .phone(transaction.getPhone())
                    .memo(transaction.getMemo())
                    .createdAt(CustomDataFormatter.of(transaction.getCreatedAt())) // yyyy-MM-dd HH:mm:ss
                    .updatedAt(CustomDataFormatter.of(transaction.getUpdatedAt())) // yyyy-MM-dd HH:mm:ss
                    .depositAccountBalance(transaction.getDepositAccountBalance()) // only used for test
                    .withdrawAccountBalance(transaction.getWithdrawAccountBalance()) // only used for test
                    .build();
        }
    }
}
