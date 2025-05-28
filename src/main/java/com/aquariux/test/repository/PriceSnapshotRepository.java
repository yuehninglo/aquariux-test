package com.aquariux.test.repository;

import com.aquariux.test.entity.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {

    @Query("SELECT s FROM PriceSnapshot s WHERE s.tradingPairId = :tradingPairId " +
           "ORDER BY capturedAt DESC LIMIT 1")
    Optional<PriceSnapshot> findByTradingPairId(Long tradingPairId);
}
