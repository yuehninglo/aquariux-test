package com.aquariux.test.repository;

import com.aquariux.test.entity.TradingPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface TradingPairRepository extends JpaRepository<TradingPair, Long> {
    Optional<TradingPair> findBySymbolIgnoreCase(String symbol);
    List<TradingPair> findBySymbolInIgnoreCase(List<String> symbols);
}
