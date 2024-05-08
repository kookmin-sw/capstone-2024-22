package com.moment.core.domain.trip;

import com.moment.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {
    Optional<Trip> findByUserAndIsNotTitledOrderByStartDate(User user, boolean b);
    Optional<Trip> findByUser_IdAndIsNotTitledOrderByStartDate(Long userId, boolean b);

    List<Trip> findAllByUserAndIsNotTitledOrderByStartDate(User user, boolean b);



}