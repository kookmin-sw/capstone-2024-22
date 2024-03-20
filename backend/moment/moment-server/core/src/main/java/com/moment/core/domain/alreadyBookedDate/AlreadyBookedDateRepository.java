package com.moment.core.domain.alreadyBookedDate;

import com.moment.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AlreadyBookedDateRepository extends JpaRepository<AlreadyBookedDate, Long> {
    boolean existsByUserIdAndYearDate(Long userId, LocalDate date);

    boolean existsByUserIdAndYearDateBetween(Long userId, LocalDate stDate, LocalDate endDate);

    void deleteByUserAndYearDateBetween(User user, LocalDate startDate, LocalDate endDate);
}