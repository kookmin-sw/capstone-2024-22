package com.moment.core.service;

import com.moment.core.domain.alreadyBookedDate.AlreadyBookedDate;
import com.moment.core.domain.alreadyBookedDate.AlreadyBookedDateRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.response.AlreadyBookedDateResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AlreadyBookedDateService {
    private final AlreadyBookedDateRepository alreadyBookedDateRepository;
    private final UserRepository userRepository;


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

    // 전부 가져오기
    public AlreadyBookedDateResponseDTO.GetAllCardView getAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("해당 유저가 없습니다."));
        List<AlreadyBookedDate> alreadyBookedDates = alreadyBookedDateRepository.findAllByUserOrderByYearDate(user);
        List<AlreadyBookedDateResponseDTO.GetCardView> cardViews = new ArrayList<>();
        for (AlreadyBookedDate alreadyBookedDate : alreadyBookedDates) {
            cardViews.add(AlreadyBookedDateResponseDTO.GetCardView.builder()
                    .bookedDate(alreadyBookedDate.getYearDate())
                    .build());
        }
        return AlreadyBookedDateResponseDTO.GetAllCardView.builder()
                .cardViews(cardViews)
                .build();
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
