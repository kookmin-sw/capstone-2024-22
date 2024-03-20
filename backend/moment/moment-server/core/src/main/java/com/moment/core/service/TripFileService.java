package com.moment.core.service;

import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.tripFile.TripFile;
import com.moment.core.domain.tripFile.TripFileRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.response.TripFileResponseDTO;
import com.moment.core.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TripFileService {
    private final TripFileRepository tripFileRepository;
    private final UserRepository userRepository;

    // 날짜를 받아서 이미 존재하면 해당 여행으로 업데이트, 없으면 새로 생성
    public Integer findByDateAndUpdate(Trip trip, LocalDate date) {
        Optional<TripFile> tripFile = this.findByUserAndYearDate(trip.getUser(), date);
        if (tripFile.isPresent()) {
            TripFile tf = tripFile.get();
            tf.setTrip(trip);
            tripFileRepository.save(tf);
            return tf.getAnalyzingCount();
        } else {
            tripFileRepository.save(TripFile.builder().trip(trip).user(trip.getUser()).analyzingCount(0).yearDate(date).build());
            return 0;
        }
    }

    public Optional<TripFile> findByUserAndYearDate(User user, LocalDate localDate) {
        return tripFileRepository.findByUserAndYearDate(user, localDate);
    }


    // 분석 중 파일 개수 증가
    public void increaseAnalyzingCount(TripFile tripFile) {
        tripFile.setAnalyzingCount(tripFile.getAnalyzingCount() + 1);
        tripFileRepository.save(tripFile);
    }

    // 분석 중 파일 개수 감소
    public void decreaseAnalyzingCount(TripFile tripFile) {
        tripFile.setAnalyzingCount(tripFile.getAnalyzingCount() - 1);
        tripFileRepository.save(tripFile);
    }

    public TripFileResponseDTO.GetAllTripFile getTripFiles(Long userId, Long tripId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("존재하지 않는 유저입니다."));
        List<TripFileResponseDTO.GetTripFile> rtnList = new ArrayList<>();
        List<TripFile> tripFiles = tripFileRepository.findByUserAndTrip_Id(user, tripId);
        for (TripFile tripFile : tripFiles) {
            rtnList.add(TripFileResponseDTO.GetTripFile.fromEntity(tripFile));
        }
        return TripFileResponseDTO.GetAllTripFile.builder().tripFiles(rtnList).build();
    }


}
