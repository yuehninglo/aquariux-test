package com.aquariux.test.repository;

import com.aquariux.test.entity.PriceSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceSnapshotRepository extends JpaRepository<PriceSnapshot, Long> {
    PriceSnapshot saveAndFlush(PriceSnapshot priceSnapshot);
}
