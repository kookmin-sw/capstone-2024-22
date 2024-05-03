package com.moment.core.service;

import com.moment.core.domain.cardView.CardViewRepository;
import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.tripFile.TripFile;
import com.moment.core.domain.tripFile.TripFileRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.response.TripFileResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TripFileService {
    private final TripFileRepository tripFileRepository;
    private final UserRepository userRepository;
    private final CardViewRepository cardViewRepository;
    private final TripFileService tripFileService;

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

    // 해당 TripFile의 카드뷰 개수 반환
    public Long getCardViewCount(TripFile tripFile) {
        return cardViewRepository.countByTripFile(tripFile);
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

    public TripFileResponseDTO.GetAllTripFile getTripFiles(Long tripId) {
        List<TripFileResponseDTO.GetTripFile> rtnList = new ArrayList<>();
        List<TripFile> tripFiles = tripFileRepository.findByTrip_IdOrderByYearDate(tripId);
        for (TripFile tripFile : tripFiles) {
            Integer totalCount = tripFileService.getCardViewCount(tripFile).intValue();
            rtnList.add(TripFileResponseDTO.GetTripFile.fromEntity(tripFile, totalCount));
        }
        return TripFileResponseDTO.GetAllTripFile.builder().tripFiles(rtnList).build();
    }


    // 여행 삭제 시 여행에 연결된 여행파일들을 삭제하거나 untitled 여행으로 옮긴다.
    // 카드뷰가 하나도 없는 여행파일은 삭제, 하나라도 있다면 untitled 여행으로 옮긴다.
    @Transactional
    public void deleteByTripOrUntitled(Trip trip, Trip untitledTrip) {
        tripFileRepository.findByTrip(trip).forEach(tripFile -> {
                    if (cardViewRepository.findByTripFile(tripFile).isEmpty()) {
                        tripFileRepository.delete(tripFile);
                    } else {
                        tripFile.setTrip(untitledTrip);
                        tripFileRepository.save(tripFile);
                    }
                }
        );
    }

    public void delete(TripFile tripFile) {
        tripFileRepository.delete(tripFile);
    }
}
