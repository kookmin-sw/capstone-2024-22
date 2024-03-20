package com.moment.core.service;

import com.moment.core.domain.alreadyBookedDate.AlreadyBookedDate;
import com.moment.core.domain.alreadyBookedDate.AlreadyBookedDateRepository;
import com.moment.core.domain.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class AlreadyBookedDateService {
    private final AlreadyBookedDateRepository alreadyBookedDateRepository;


    // save
    public void save(AlreadyBookedDate alreadyBookedDate) {
        alreadyBookedDateRepository.save(alreadyBookedDate);
    }

    // add
    @Transactional
    public void addAll(User user, LocalDate stDate, LocalDate endDate) {
        for (LocalDate date = stDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            AlreadyBookedDate alreadyBookedDate = AlreadyBookedDate.builder()
                    .user(user)
                    .yearDate(date)
                    .build();
            alreadyBookedDateRepository.save(alreadyBookedDate);
        }
    }

    // delete
    public void delete(Long alreadyBookedDateId) {
        alreadyBookedDateRepository.deleteById(alreadyBookedDateId);
    }

    // 해당 날짜에 이미 예약된 여행이 있는지 확인
    public boolean isAlreadyBookedDate(Long userId, LocalDate stDate, LocalDate endDate) {
        // stDate부터 endDate까지의 날짜사이에 이미 예약된 여행이 있는지 확인
        return alreadyBookedDateRepository.existsByUserIdAndYearDateBetween(userId, stDate, endDate);
    }

    public void deleteAll(User user, LocalDate startDate, LocalDate endDate) {
        alreadyBookedDateRepository.deleteByUserAndYearDateBetween(user, startDate, endDate);
    }
}
