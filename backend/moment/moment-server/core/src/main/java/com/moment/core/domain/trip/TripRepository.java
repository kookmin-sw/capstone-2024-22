package com.moment.core.domain.trip;

import com.moment.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByUserAndIsNotTitled(User user, boolean b);
    Optional<Trip> findByUser_IdAndIsNotTitled(Long userId, boolean b);

    List<Trip> findAllByUserAndIsNotTitledOrderByStartDate(User user, boolean b);
}