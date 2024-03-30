package com.cos.bank.currency.repository;

import com.cos.bank.currency.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Currency findByCurrencyCode(String searchCurrencyCode);
}
