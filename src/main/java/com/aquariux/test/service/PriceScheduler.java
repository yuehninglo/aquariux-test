package com.aquariux.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@Slf4j
public class PriceScheduler {
    private final PriceService priceService;

    @Autowired
    public PriceScheduler(PriceService priceService) {
        this.priceService = priceService;
    }

//    @Scheduled(fixedRate = 10000) // 10-second interval
//    public void scheduledPriceFetch() {
//        log.info("Starting scheduled price fetch at: {}", LocalDateTime.now());
//        priceService.fetchAndStoreBestPrices();
//    }
}
