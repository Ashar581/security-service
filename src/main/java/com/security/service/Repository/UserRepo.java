package com.security.service.Repository;

import com.security.service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(@Param("email") String email);
    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM Users u WHERE u.email = :email",nativeQuery = true)
    Boolean existsByEmail(@Param("email") String email);
}
