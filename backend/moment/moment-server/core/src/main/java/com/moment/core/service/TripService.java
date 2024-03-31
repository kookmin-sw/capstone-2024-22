package com.moment.core.service;

import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.trip.TripRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.request.TripRequestDTO;
import com.moment.core.dto.response.TripResponseDTO;
import com.moment.core.exception.AlreadyBookedDateException;
import com.moment.core.exception.UntitledTripDeleteException;
import com.moment.core.exception.UserNotFoundException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final AlreadyBookedDateService alreadyBookedDateService;
    private final TripFileService tripFileService;
    private final EntityManager em;

    public void save(Trip trip) {
        tripRepository.save(trip);
    }

    // 1. 여행을 삭제한다.
    // 2. 엮여있는 여행파일들 탐색
    // 3. 내부가 비어있는 여행파일들은 그냥 삭제, 내부가 비어있지 않다면 untitled 여행으로 넣는다.
    // 4. alreadyBookedDate에서 삭제한다.
    @Transactional
    public Trip delete(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("존재하지 않는 여행입니다."));
        if(trip.getIsNotTitled()){
            throw new UntitledTripDeleteException("묶이지 않은 여행은 삭제할 수 없습니다.");
        }
        Trip untitledTrip = getUntitledTrip(trip.getUser());
        tripFileService.deleteByTripOrUntitled(trip, untitledTrip);
        alreadyBookedDateService.deleteAll(trip.getUser(), trip.getStartDate(), trip.getEndDate());
        tripRepository.delete(trip);
        return trip;
    }

    @Transactional
    public void update(Long userId, TripRequestDTO.UpdateTrip trip) {
        Trip oldTrip = this.delete(trip.getTripId());
        this.register(TripRequestDTO.RegisterTrip.builder()
                .startDate(oldTrip.getStartDate())
                .endDate(oldTrip.getEndDate())
                .tripName(oldTrip.getTripName())
                .build(), userId);
    }


    // 여행 등록
    @Transactional
    public void register(TripRequestDTO.RegisterTrip registerTrip, Long userId) {
        LocalDate stDate = registerTrip.getStartDate();
        LocalDate endDate = registerTrip.getEndDate();
        Integer analyzingCount = 0;
        if (stDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜가 종료 날짜보다 늦을 수 없습니다.");
        }

        // 유저 검증
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));

        // 여행 등록 날짜를 이미 등록했는지 검증
        if (alreadyBookedDateService.isAlreadyBookedDate(user.getId(), stDate, endDate)) {
            throw new AlreadyBookedDateException("이미 예약된 날짜입니다.");
        }

        // 여행 생성
        Trip trip = Trip.builder()
                .user(user)
                .startDate(stDate)
                .endDate(endDate)
                .tripName(registerTrip.getTripName())
                .isNotTitled(false)
                .analyzingCount(0)
                .build();
        em.persist(trip);

        // 여행 파일들이 있는지 확인하고 생성
        for (LocalDate date = stDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            log.info("date: {}", date);
            // 이미 해당 날짜로 된 파일이 있는지 확인
            analyzingCount += tripFileService.findByDateAndUpdate(trip, date);
        }

        trip.setAnalyzingCount(analyzingCount);

        // 여행 등록 날짜에 추가
        alreadyBookedDateService.addAll(user, registerTrip.getStartDate(), registerTrip.getEndDate());

        tripRepository.save(trip);
    }

    // 묶이지 않은 여행 가져오기
    public Trip getUntitledTrip(User user) {
        return tripRepository.findByUserAndIsNotTitledOrderByStartDate(user, true)
                .orElseThrow(() -> new RuntimeException("묶이지 않은 기록이 존재하지 않음."));
    }

    // 묶이지 않은 여행 가져오기 userId로
    public Trip getUntitledTripById(Long userId) {
        return tripRepository.findByUser_IdAndIsNotTitledOrderByStartDate(userId, true)
                .orElseThrow(() -> new RuntimeException("묶이지 않은 기록이 존재하지 않음."));
    }

    // 분석 중 파일 개수 증가
    public void increaseAnalyzingCount(Trip trip) {
        trip.setAnalyzingCount(trip.getAnalyzingCount() + 1);
        tripRepository.save(trip);
    }

    // 분석 중 파일 개수 감소
    public void decreaseAnalyzingCount(Trip trip) {
        trip.setAnalyzingCount(trip.getAnalyzingCount() - 1);
        tripRepository.save(trip);
    }

    public TripResponseDTO.GetAllTrip getAllTrip(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        List<TripResponseDTO.GetTrip> rtnList = new ArrayList<>();
        List<Trip> trips = tripRepository.findAllByUserAndIsNotTitledOrderByStartDate(user, false);
        for (Trip trip : trips) {
            rtnList.add(TripResponseDTO.GetTrip.fromEntity(trip));
        }
        return TripResponseDTO.GetAllTrip.builder().trips(rtnList).build();
    }
}
