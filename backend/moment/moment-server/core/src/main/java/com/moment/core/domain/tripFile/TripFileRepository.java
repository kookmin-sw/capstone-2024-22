package com.moment.core.domain.tripFile;

import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripFileRepository extends JpaRepository<TripFile, Long> {

    Optional<TripFile> findByUserAndYearDate(User user, LocalDate localDate);


    List<TripFile> findByTrip_Id(Long tripId);



    List<TripFile> findByTrip(Trip trip);
}