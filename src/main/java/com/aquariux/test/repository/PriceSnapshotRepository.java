package com.aquariux.test.repository;

import com.aquariux.test.entity.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    @Query("SELECT * FROM price_snapshots s WHERE s.trading_pair_id =: tradingPairId" +
           "ORDER BY captured_at DESC LIMIT 1")
    Optional<PriceSnapshot> findByTradingPairId(Long tradingPairId);
}
