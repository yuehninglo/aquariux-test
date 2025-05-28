package com.aquariux.test.service;

import com.aquariux.test.dto.BinanceTickerResponse;
import com.aquariux.test.dto.HuobiResponse;
import com.aquariux.test.dto.HuobiTicker;
import com.aquariux.test.dto.PriceData;
import com.aquariux.test.entity.PriceSnapshot;
import com.aquariux.test.entity.PriceSource;
import com.aquariux.test.entity.TradingPair;
import com.aquariux.test.repository.PriceSnapshotRepository;
import com.aquariux.test.repository.PriceSourceRepository;
import com.aquariux.test.repository.TradingPairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class PriceService {
    private final RestTemplate restTemplate;
    private final PriceSourceRepository priceSourceRepository;
    private final TradingPairRepository tradingPairRepository;
    private final PriceSnapshotRepository priceSnapshotRepository;

    private static final List<String> TARGET_PAIRS = Arrays.asList("BTCUSDT", "ETHUSDT");

    @Autowired
    public PriceService(RestTemplate restTemplate,
                        PriceSourceRepository priceSourceRepository,
                        TradingPairRepository tradingPairRepository,
                        PriceSnapshotRepository priceSnapshotRepository) {
        this.restTemplate = restTemplate;
        this.priceSourceRepository = priceSourceRepository;
        this.tradingPairRepository = tradingPairRepository;
        this.priceSnapshotRepository = priceSnapshotRepository;
    }

    public void fetchAndStoreBestPrices() {
        try {
            log.info("Starting price fetch and comparison process");
            List<PriceSource> sources = priceSourceRepository.findAll();
            Map<String, List<PriceData>> pricesBySymbol = new HashMap<>();
            for (PriceSource source : sources) {
                try {
                    List<PriceData> prices = fetchPricesFromSource(source);
                    for (PriceData price : prices) {
                        pricesBySymbol.computeIfAbsent(price.getSymbol().toUpperCase(),
                                k -> new ArrayList<>()
                        ).add(price);
                    }
                } catch (Exception e) {
                    log.error("Failed to fetch prices from source: {}", source.getName(), e);
                }
            }

            // Find the best prices and store
            for (String symbol : TARGET_PAIRS) {
                List<PriceData> prices = pricesBySymbol.get(symbol);
                if (prices != null && !prices.isEmpty()) {
                    storeBestPrice(symbol, prices);
                } else {
                    log.warn("No prices found for symbol: {}", symbol);
                }
            }
            log.info("Completed price fetch and storage process");
        } catch (Exception e) {
            log.error("Error in fetchAndStoreBestPrices", e);
        }
    }

    private List<PriceData> fetchPricesFromSource(PriceSource source) {
        List<PriceData> prices = new ArrayList<>();
        try {
            if (source.getName().equalsIgnoreCase("Binance")) {
                prices = fetchBinancePrices(source);
            } else {
                prices = fetchHuobiPrices(source);
            }
        } catch (Exception e) {
            log.error("Error fetching prices from {}: {}", source.getName(), e.getMessage());
        }
        return prices;
    }

    private List<PriceData> fetchBinancePrices(PriceSource source) {
        List<PriceData> prices = new ArrayList<>();
        try {
            BinanceTickerResponse[] response = restTemplate.getForObject(
                    source.getUrl(), BinanceTickerResponse[].class);
            if (response != null) {
                for (BinanceTickerResponse ticker : response) {
                    if (TARGET_PAIRS.contains(ticker.getSymbol().toUpperCase())) {
                        prices.add(new PriceData(
                                        ticker.getSymbol(),
                                        new BigDecimal(ticker.getBidPrice()),
                                        new BigDecimal(ticker.getAskPrice()),
                                        source.getName()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error parsing Binance response", e);
            throw e;
        }
        return prices;
    }

    private List<PriceData> fetchHuobiPrices(PriceSource source) {
        List<PriceData> prices = new ArrayList<>();
        try {
            HuobiResponse response = restTemplate.getForObject(source.getUrl(), HuobiResponse.class);
            if (response != null && "ok".equals(response.getStatus()) && response.getData() != null) {
                for (HuobiTicker ticker : response.getData()) {
                    if (TARGET_PAIRS.contains(ticker.getSymbol().toUpperCase())) {
                        prices.add(new PriceData(
                                        ticker.getSymbol(),
                                        ticker.getBid(),
                                        ticker.getAsk(),
                                        source.getName()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error parsing Huobi response", e);
            throw e;
        }
        return prices;
    }

    private void storeBestPrice(String symbol, List<PriceData> prices) {
        try {
            PriceData bestBid = prices.stream()
                    .max(Comparator.comparing(PriceData::getBidPrice))
                    .orElse(null);
            PriceData bestAsk = prices.stream()
                    .min(Comparator.comparing(PriceData::getAskPrice))
                    .orElse(null);
            Optional<TradingPair> tradingPair = tradingPairRepository.findBySymbolIgnoreCase(symbol);
            if (tradingPair.isPresent()) {
                Long tradingPairId = tradingPair.get().getId();
                PriceSnapshot snap
                        = priceSnapshotRepository.findByTradingPairId(tradingPairId)
                                                 .orElse(new PriceSnapshot(tradingPairId));
                if (bestBid != null) {
                    snap.setBidPrice(bestBid.getBidPrice());
                    priceSnapshotRepository.save(snap);
                    log.info("Stored best bid price for {}: {} from {}",
                            symbol, bestBid.getBidPrice(), bestBid.getSource());
                }
                if (bestAsk != null) {
                    snap.setAskPrice(bestAsk.getAskPrice());
                    log.info("Stored best ask price for {}: {} from {}",
                            symbol, bestAsk.getAskPrice(), bestAsk.getSource());
                }
                priceSnapshotRepository.save(snap);
            } else {
                log.warn("Trading pair not found for symbol: {}", symbol);
            }

        } catch (Exception e) {
            log.error("Error storing best price for symbol: {}", symbol, e);
        }
    }

    private Long getSourceIdByName(String sourceName) {
        return priceSourceRepository.findAll().stream()
                .filter(source -> source.getName().equalsIgnoreCase(sourceName))
                .map(PriceSource::getId)
                .findFirst()
                .orElse(null);
    }
}
