package com.cos.bank.currency.dto;


import com.cos.bank.currency.domain.Currency;
import com.cos.bank.util.CustomDataFormatter;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateListDto {
    private List<ExchangeRateListDto.ExchangeRateDto> exchangeRateDtos;

    public static ExchangeRateListDto of(List<Currency> currencyList) {
        return ExchangeRateListDto.builder()
                .exchangeRateDtos(currencyList.stream().map(ExchangeRateListDto.ExchangeRateDto::of).collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExchangeRateDto {

        private String currencyCode;
        private BigDecimal exchangeRate;
        private String updatedAt;

        public static ExchangeRateListDto.ExchangeRateDto of(Currency currency){
            return ExchangeRateListDto.ExchangeRateDto.builder()
                    .currencyCode(currency.getCurrencyCode())
                    .exchangeRate(currency.getExchangeRate())
                    .updatedAt(CustomDataFormatter.of(currency.getUpdatedAt()))
                    .build();
        }
    }
}
