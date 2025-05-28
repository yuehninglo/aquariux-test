package com.aquariux.test.controller;

import com.aquariux.test.dto.PriceData;
import com.aquariux.test.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/price")
@Slf4j
public class PriceController {
    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("best/{symbol}")
    public ResponseEntity<PriceData> getLatestBestPrices(@PathVariable String symbol) {
        PriceData data = priceService.getLatestBestPriceBySymbol(symbol);
        return ResponseEntity.ok(data);
    }
}