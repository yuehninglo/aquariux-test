package com.aquariux.test.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeResponse {

    private Long id;
    private String tradingPairSymbol;
    private String side;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal total;
    private LocalDateTime createdAt;
}
