package com.aquariux.test.repository;

import com.aquariux.test.entity.Transaction;
import com.aquariux.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.tradingPair tp " +
           "JOIN FETCH tp.baseCurrencyId JOIN FETCH tp.quoteCurrencyId " +
           "WHERE t.user = :user ORDER BY t.createdAt DESC")
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}
