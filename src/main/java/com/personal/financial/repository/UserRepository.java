package com.personal.financial.repository;

import com.personal.financial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE(u.userName = :identifier OR u.email = :identifier) AND u.isActive = true")
    Optional<User> findByUserNameOrEmailAndIsActive(@Param("identifier") String identifier);


    boolean existsByEmail(String email);

    boolean existsByUserName(String userName);
}
