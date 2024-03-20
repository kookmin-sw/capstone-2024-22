package com.moment.core.service;

import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.trip.TripRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.request.UserRequestDTO;
import com.moment.core.exception.UserAlreadyExistException;
import com.moment.core.exception.UserNotValidException;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TripService tripService;
    private final TripRepository tripRepository;
    private final EntityManager em;

    @Transactional
    public User save(UserRequestDTO.registerUser request) {
        Boolean isAlreadyExist = userRepository.existsByEmail(request.getEmail());
        if (isAlreadyExist) {
            throw new UserAlreadyExistException("이미 존재하는 아이디입니다.");
        }
        User user = User.builder()
                .id(request.getId())
                .email(request.getEmail())
                .build();
//        em.persist(user);
        User managedUser = em.merge(user);
        tripService.save(Trip.builder()
                .user(managedUser)
                        .analyzingCount(0)
                        .startDate(null)
                        .endDate(null)
                        .tripName("untitled trip")
                        .isNotTitled(true)
                .build()
        );
        return userRepository.save(managedUser);
    }

    public void validateUserWithTrip(Long userId, Long tripId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 여행입니다."));
        if (!trip.getUser().equals(user)) {
            throw new UserNotValidException("해당 여행은 유저의 여행이 아닙니다.");
        }
    }
}
