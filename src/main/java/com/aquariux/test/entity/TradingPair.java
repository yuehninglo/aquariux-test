package com.aquariux.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trading_pairs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_currency_id", nullable = false)
    private Long baseCurrencyId;

    @Column(name = "quote_currency_id", nullable = false)
    private Long quoteCurrencyId;

    @Column(nullable = false, unique = true, length = 20)
    private String symbol;
}