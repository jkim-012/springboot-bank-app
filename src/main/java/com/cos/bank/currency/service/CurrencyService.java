package com.cos.bank.currency.service;

import com.cos.bank.currency.dto.ExchangeRateDto;
import com.cos.bank.currency.dto.ExchangeRateListDto;

public interface CurrencyService {

    ExchangeRateListDto getAllExchangeRates();
    ExchangeRateDto.Response getExchangeRate(String searchCurrencyCode);
}
