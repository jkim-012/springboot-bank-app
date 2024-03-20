package com.cos.bank.transaction.dto;

import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListDto {

    private List<TransactionDto> transactionDtos = new ArrayList<>();

    public static TransactionListDto of(List<Transaction> transactions, Long accountId) {
        List<TransactionDto> dtos =
                transactions.stream()
                        .map((transaction) -> TransactionDto.of(transaction, accountId))
                        .collect(Collectors.toList());
        return TransactionListDto.builder().transactionDtos(dtos).build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionDto {

        private Long id;
        private TransactionType transactionType;
        private BigDecimal amount;
        private String sender;
        private String receiver;
        private String phone;
        private LocalDateTime createdAt;
        private BigDecimal balance;

        public static TransactionDto of(Transaction transaction, Long accountId) {
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

            return TransactionDto.builder()
                    .id(transaction.getId())
                    .transactionType(transaction.getTransactionType())
                    .amount(transaction.getAmount())
                    .sender(transaction.getSender())
                    .receiver(transaction.getReceiver())
                    .phone(transaction.getPhone())
                    .createdAt(transaction.getCreatedAt())
                    .balance(balance)
                    .build();
        }
    }
}
