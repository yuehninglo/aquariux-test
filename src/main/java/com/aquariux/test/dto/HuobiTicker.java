package com.aquariux.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HuobiTicker {
    private String symbol;
    private BigDecimal bid;
    private BigDecimal ask;
}
