package com.aquariux.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "price_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceSnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trading_pair_id", nullable = false)
    private Long tradingPairId;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(nullable = false, precision = 36, scale = 18)
    private BigDecimal price;

    @Column(name = "captured_at")
    @CreationTimestamp
    private LocalDateTime capturedAt;
}
