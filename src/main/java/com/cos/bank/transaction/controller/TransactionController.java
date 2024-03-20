package com.cos.bank.transaction.controller;


import com.cos.bank.config.auth.LoginUser;
import com.cos.bank.transaction.dto.TransactionDetailDto;
import com.cos.bank.transaction.dto.TransactionListDto;
import com.cos.bank.transaction.service.TransactionService;
import com.cos.bank.util.ResponseDto;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok()
                .body(new ResponseDto<>(1, "Transactions retrieved successfully.", transactionList));
    }

    @GetMapping("/accounts/{accountId}/transactions/{transactionId}")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long accountId,
            @PathVariable Long transactionId,
            @AuthenticationPrincipal LoginUser loginUser){

        TransactionDetailDto.Response response =
                transactionService.getTransaction(accountId, transactionId, loginUser.getUser().getId());
        return ResponseEntity.ok()
                .body(new ResponseDto<>(1, "Transaction retrieved successfully.", response));
    }
}
