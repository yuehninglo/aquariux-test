package com.aquariux.test.controller;

import com.aquariux.test.service.PriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/price")
@Slf4j
public class PriceController {
    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("refresh")
    public ResponseEntity.BodyBuilder getLatestPrices() {
        priceService.fetchAndStoreBestPrices();
        return ResponseEntity.ok();
    }

}

