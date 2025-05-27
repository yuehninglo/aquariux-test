package com.aquariux.test.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BinanceTickerResponse {
    private String symbol;
    private String bidPrice;
    private String askPrice;
}
