package com.cos.bank.transaction.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionType {
    WITHDRAW("Withdraw"),
    DEPOSIT("Deposit"),
    TRANSFER("Transfer"),
    ALL("Transaction History");

    private final String value;


}
