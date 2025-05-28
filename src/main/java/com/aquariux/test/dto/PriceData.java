package com.aquariux.test.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PriceData {
    private String symbol;
    private BigDecimal bidPrice;  // For the SELL orders
    private BigDecimal askPrice;  // For the BUY orders
    private String source;
}
