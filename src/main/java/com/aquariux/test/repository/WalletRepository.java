package com.aquariux.test.repository;

import com.aquariux.test.entity.Cryptocurrency;
import com.aquariux.test.entity.User;
import com.aquariux.test.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query("SELECT w FROM Wallet w JOIN FETCH w.crypto WHERE w.user = :user")
    List<Wallet> findByUser(User user);

    @Modifying
    @Query("UPDATE Wallet w SET balance = :newBalance WHERE w.user = :user AND crypto = :crypto")
    int updateByUserAndCrypto(@Param("user") User user,
                              @Param("crypto") Cryptocurrency crypto,
                              @Param("newBalance") BigDecimal newBalance);
}
