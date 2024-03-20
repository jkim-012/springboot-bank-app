package com.cos.bank.transaction.dto;

import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.cos.bank.util.CustomDataFormatter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDetailDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private Long id;
        private TransactionType transactionType;
        private BigDecimal amount;
        private String sender;
        private String receiver;
        private String phone;
        private String memo;
        private String createdAt;
        private String updatedAt;
        private BigDecimal balance;

        @JsonIgnore
        private BigDecimal depositAccountBalance;
        @JsonIgnore
        private BigDecimal withdrawAccountBalance;

        public static TransactionDetailDto.Response of(Transaction transaction, Long accountId) {

            BigDecimal balance = BigDecimal.valueOf(0.0);
            // (withdraw account = value, deposit account = null)
            if (transaction.getDepositAccount() == null) {
                balance = transaction.getWithdrawAccountBalance();
            } else if (transaction.getWithdrawAccount() == null) {
                // (withdraw account = null, deposit account = value)
                balance = transaction.getDepositAccountBalance();
            } else {
                // (withdraw account = value, deposit account = value)
                if (accountId.equals(transaction.getDepositAccount().getId())) {
                    balance = transaction.getDepositAccountBalance();
                } else {
                    balance = transaction.getWithdrawAccountBalance();
                }
            }

            return Response.builder()
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
                    .balance(balance)
                    .build();
        }
    }
}
