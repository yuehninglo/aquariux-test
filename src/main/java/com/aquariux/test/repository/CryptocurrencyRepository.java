package com.aquariux.test.repository;

import com.aquariux.test.entity.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, Long> {
    Cryptocurrency findBySymbol(String symbol);
}
