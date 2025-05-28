package com.aquariux.test.controller;

import com.aquariux.test.dto.TradeRequest;
import com.aquariux.test.dto.TradeResponse;
import com.aquariux.test.service.TradingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trade")
@Slf4j
public class TradeController {

    private final TradingService tradingService;

    @Autowired
    public TradeController(TradingService tradingService) {
        this.tradingService = tradingService;
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
