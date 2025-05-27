package com.aquariux.test.repository;

import com.aquariux.test.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.tradingPair tp " +
           "JOIN FETCH tp.baseCurrency JOIN FETCH tp.quoteCurrency " +
           "WHERE t.userId = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
