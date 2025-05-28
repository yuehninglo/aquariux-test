package com.aquariux.test.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletBalanceResponse {
    private String cryptoSymbol;
    private String cryptoName;
    private BigDecimal balance;
}
