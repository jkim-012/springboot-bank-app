package com.cos.bank.transaction.controller;


import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.transaction.dto.TransactionListDto;
import com.cos.bank.transaction.service.TransactionService;
import com.cos.bank.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<?> getTransactionList(
            @PathVariable Long accountId,
            @RequestParam(value = "transactionType", defaultValue = "ALL") String transactionType,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @AuthenticationPrincipal LoginUser loginUser){

        TransactionListDto transactionList
                = transactionService.getTransactionList(loginUser.getUser().getId(), accountId, transactionType, page);
        return new ResponseEntity<>(
                new ResponseDto<>(1, "Transactions retrieved successfully.", transactionList), HttpStatus.OK);
    }
}
