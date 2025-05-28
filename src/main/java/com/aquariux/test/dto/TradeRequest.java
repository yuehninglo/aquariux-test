package com.aquariux.test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeRequest {

    private String tradingSymbol;
    private String side; // BUY or SELL
    private BigDecimal amount;
}
