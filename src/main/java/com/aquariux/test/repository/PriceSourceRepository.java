package com.aquariux.test.repository;

import com.aquariux.test.entity.PriceSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PriceSourceRepository extends JpaRepository<PriceSource, Long> {
    List<PriceSource> findAll();
}
