package com.moment.core.service;

import com.moment.core.domain.cardView.CardView;
import com.moment.core.domain.cardView.CardViewRepository;
import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.trip.TripRepository;
import com.moment.core.domain.tripFile.TripFile;
import com.moment.core.domain.tripFile.TripFileRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.request.UserRequestDTO;
import com.moment.core.exception.UserAlreadyExistException;
import com.moment.core.exception.UserNotValidException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TripService tripService;
    private final TripRepository tripRepository;
    private final EntityManager em;
    private final TripFileRepository tripFileRepository;
    private final CardViewRepository cardViewRepository;

    @Transactional
    public User save(UserRequestDTO.registerUser request) {
        Boolean isAlreadyExist = userRepository.existsByEmail(request.getEmail());
        if (isAlreadyExist) {
            throw new UserAlreadyExistException("이미 존재하는 아이디입니다.");
        }
        log.info("유저 등록을 시도합니다.");
        log.info("user : {}", request.getId());
//        User user = User.builder()
//                .id(request.getId())
//                .email(request.getEmail())
//                .notification(request.isNotification())
//                .dataUsage(request.isDataUsage())
//                .firebaseToken(request.getFirebaseToken())
//                .build();
//        log.info("user : {}", user.getId());
//        User mu = userRepository.save(user);
        userRepository.saveN(request.getId(), LocalDateTime.now(), LocalDateTime.now(), request.getEmail(), request.isNotification(), request.isDataUsage(), request.getFirebaseToken());
        return userRepository.findById(request.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
    }

    public void validateUserWithTrip(Long userId, Long tripId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행입니다."));
        if (!trip.getUser().equals(user)) {
            throw new UserNotValidException("해당 여행은 유저의 여행이 아닙니다.");
        }
    }

    public void validateUserWithTripFile(Long userId, Long tripFileId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        TripFile tripFile = tripFileRepository.findById(tripFileId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행입니다."));
        if (!tripFile.getUser().equals(user)) {
            throw new UserNotValidException("해당 여행파일은 유저의 여행파일이 아닙니다.");
        }
    }

    public void validateUserWithCardView(Long userId, Long cardViewId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        CardView cardView = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드뷰입니다."));
        if (!cardView.getTripFile().getUser().equals(user)) {
            throw new UserNotValidException("해당 카드뷰는 유저의 카드뷰가 아닙니다.");
        }
    }

    public void updateUserSetting(UserRequestDTO.updateUser request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        user.setNotification(request.isNotification());
        user.setDataUsage(request.isDataUsage());
        user.setFirebaseToken(request.getFirebaseToken());
        userRepository.save(user);
    }
}
