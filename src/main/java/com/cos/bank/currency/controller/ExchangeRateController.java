package com.cos.bank.currency.controller;


import com.cos.bank.currency.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    // API to fetch all exchange rates from open API
    @GetMapping("/test")
    public void getAllExchangeRates() {
//        exchangeRateService.getAllExchangeRates();
    }





}
