package com.cos.bank.transaction.service;

import com.cos.bank.transaction.dto.TransactionDetailDto;
import com.cos.bank.transaction.dto.TransactionListDto;

public interface TransactionService {
    TransactionListDto getTransactionList(Long userId, Long accountId, String transactionType, Integer page);
    TransactionDetailDto.Response getTransaction(Long accountId, Long transactionId, Long userId);
}
