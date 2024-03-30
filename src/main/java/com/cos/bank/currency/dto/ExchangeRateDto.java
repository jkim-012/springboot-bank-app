package com.cos.bank.currency.dto;

import com.cos.bank.currency.domain.Currency;
import com.cos.bank.util.CustomDataFormatter;
import lombok.*;

import java.math.BigDecimal;

public class ExchangeRateDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String currencyCode;
        private BigDecimal exchangeRate;
        private String updatedAt;

        public static ExchangeRateDto.Response of(Currency currency){
            return ExchangeRateDto.Response.builder()
                    .currencyCode(currency.getCurrencyCode())
                    .exchangeRate(currency.getExchangeRate())
                    .updatedAt(CustomDataFormatter.of(currency.getUpdatedAt()))
                    .build();
        }
    }
}
