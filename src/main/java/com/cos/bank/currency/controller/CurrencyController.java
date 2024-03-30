package com.cos.bank.currency.controller;


import com.cos.bank.currency.dto.ExchangeRateListDto;
import com.cos.bank.currency.service.CurrencyService;
import com.cos.bank.util.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CurrencyController {

    private final CurrencyService exchangeRateService;

    // reading all exchange rates API
    @GetMapping("/exchange-rates")
    public ResponseEntity<?> getAllExchangeRates() {
        ExchangeRateListDto exchangeRateListDto = exchangeRateService.getAllExchangeRates();
        return ResponseEntity.ok()
                .body(new ResponseDto(1, "Exchange rates retrieved successfully.", exchangeRateListDto));
    }


    // API to read one exchange rate





}
