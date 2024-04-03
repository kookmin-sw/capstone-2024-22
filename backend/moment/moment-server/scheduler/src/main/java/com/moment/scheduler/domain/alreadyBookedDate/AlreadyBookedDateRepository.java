package com.moment.scheduler.domain.alreadyBookedDate;

import com.moment.scheduler.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlreadyBookedDateRepository extends JpaRepository<AlreadyBookedDate, Long> {
    boolean existsByUserIdAndYearDate(Long userId, LocalDate date);

    boolean existsByUserIdAndYearDateBetween(Long userId, LocalDate stDate, LocalDate endDate);

    void deleteByUserAndYearDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<AlreadyBookedDate> findAllByUserOrderByYearDate(User user);
}