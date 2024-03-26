package com.cos.bank.currency.repository;

import com.cos.bank.currency.domain.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
