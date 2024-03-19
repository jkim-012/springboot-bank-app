package com.cos.bank.transaction.dto;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.dto.AccountDto;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.domain.TransactionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private String sender;
    private String receiver;
    private String phone;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @JsonIgnore
    private Double depositAccountBalance;
    @JsonIgnore
    private Double withdrawAccountBalance;

    public static TransactionDto of(Transaction transaction){
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .sender(transaction.getSender())
                .receiver(transaction.getReceiver())
                .phone(transaction.getPhone())
                .memo(transaction.getMemo())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .depositAccountBalance(transaction.getDepositAccountBalance()) // only used for test
                .withdrawAccountBalance(transaction.getWithdrawAccountBalance()) // only used for test
                .build();
    }
}
