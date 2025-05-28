package com.aquariux.test.repository;

import com.aquariux.test.entity.User;
import com.aquariux.test.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w JOIN FETCH w.crypto " +
           "WHERE w.user = :user")
    List<Wallet> findByUser(User user);
}
