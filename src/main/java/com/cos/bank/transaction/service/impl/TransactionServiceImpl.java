package com.cos.bank.transaction.service.impl;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.handler.exception.CustomApiException;
import com.cos.bank.handler.exception.CustomForbiddenException;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.transaction.dto.TransactionListDto;
import com.cos.bank.transaction.repository.TransactionRepository;
import com.cos.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public TransactionListDto getTransactionList
            (Long userId, Long accountId, String transactionType, Integer page) {
        // find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomApiException("Account not found."));

        // check authority
        if (!account.getUser().getId().equals(userId)) {
            throw new CustomForbiddenException("Unauthorized: You do not have permission to read transactions");
        }
        // get transactions
        List<Transaction> transactions = transactionRepository.findTransactionList(accountId, transactionType, page);
        return TransactionListDto.of(transactions, accountId);
    }
}
