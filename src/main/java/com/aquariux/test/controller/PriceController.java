package com.aquariux.test.controller;

import com.aquariux.test.dto.PriceData;
import com.aquariux.test.dto.TradeRequest;
import com.aquariux.test.dto.TradeResponse;
import com.aquariux.test.service.PriceService;
import com.aquariux.test.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price")
@Slf4j
public class PriceController {
    private final PriceService priceService;
    private final TradingService tradingService;

    @Autowired
    public PriceController(PriceService priceService,
                           TradingService tradingService) {
        this.priceService = priceService;
        this.tradingService = tradingService;
    }

    @GetMapping("best/{symbol}")
    public ResponseEntity<PriceData> getLatestBestPrices(@PathVariable String symbol) {
        PriceData data = priceService.getLatestBestPriceBySymbol(symbol);
        return ResponseEntity.ok(data);
    }

    @PostMapping("execute/{username}")
    public ResponseEntity<TradeResponse> executeTrade(@PathVariable String username,
                                                      @RequestBody TradeRequest request) {
        log.info("Received trade request: {}", request);
        try {
            TradeResponse response = tradingService.executeTrade(username, request);
            log.info("Trade executed successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error executing trade: ", e);
            return ResponseEntity.internalServerError().body(null);
        }
    }
}

