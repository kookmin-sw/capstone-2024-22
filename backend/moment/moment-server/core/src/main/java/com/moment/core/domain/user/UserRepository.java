package com.moment.core.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);


    @Query(value = "INSERT INTO user (id, createdAt, updatedAt, email, notification, data_usage, firebase_token) VALUES (:id, :createdAt, :updatedAt, :email, :notification, :dataUsage, :firebaseToken)", nativeQuery = true)
    @Modifying
    void saveN(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email, boolean notification, boolean dataUsage, String firebaseToken);

}