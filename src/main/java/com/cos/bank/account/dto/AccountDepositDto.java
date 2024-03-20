package com.cos.bank.account.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.util.CustomDataFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;


public class AccountDepositDto {

    @Getter
    @Setter
    public static class Request {

        @NotNull(message = "Deposit account number is a required field.")
        @Digits(integer = 10, fraction = 0, message = "Deposit account number must be exactly 10 digits.")
        private Long depositAccountNumber;

        @NotNull(message = "Deposit amount is a required field.")
        private BigDecimal amount;

        @NotNull(message = "Transaction type is a required field.")
        private TransactionType transactionType; // DEPOSIT

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
    public static class Response {
        private Long depositAccountId;
        private Long depositAccountNumber;
        private TransactionDto transactionDto;

        public static Response of(Account account, Transaction transaction) {
            return Response.builder()
                    .depositAccountId(account.getId())
                    .depositAccountNumber(account.getNumber())
                    .transactionDto(AccountDepositDto.TransactionDto.of(transaction))
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

        public static AccountDepositDto.TransactionDto of(Transaction transaction) {
            return AccountDepositDto.TransactionDto.builder()
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
