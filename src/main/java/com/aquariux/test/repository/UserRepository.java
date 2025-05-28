package com.aquariux.test.repository;

import com.aquariux.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    User findByUserName(String userName);
    User getReferenceById(@Param("userId") Long userId);
}
