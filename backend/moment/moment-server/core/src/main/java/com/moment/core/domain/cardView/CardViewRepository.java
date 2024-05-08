package com.moment.core.domain.cardView;

import com.moment.core.domain.tripFile.TripFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardViewRepository extends JpaRepository<CardView, Long> {
    List<CardView> findAllByTripFile_IdOrderByRecordedAt(Long tripFileId);

    List<CardView> findByTripFile(TripFile tripFile);

    Long countByTripFile(TripFile tripFile);

    List<CardView> findByTripFile_User_IdAndIsLovedOrderByRecordedAt(Long userId, Boolean isLoved);

    List<CardView> findAllByTripFile(TripFile tripFile);

    Long countByTripFile_Trip_Id(Long tripId);

    @Override
    @EntityGraph(attributePaths = {"tripFile"})
    Optional<CardView> findById(Long id);
}