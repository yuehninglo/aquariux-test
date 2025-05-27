package com.aquariux.test.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceData {
    private String symbol;
    private BigDecimal bidPrice;  // For SELL orders
    private BigDecimal askPrice;  // For BUY orders
    private String source;
}
