package com.moment.core.domain.cardView;

import com.moment.core.domain.tripFile.TripFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CardViewRepository extends JpaRepository<CardView, Long> {
    List<CardView> findAllByTripFile_Id(Long tripFileId);

    List<CardView> findByTripFile(TripFile tripFile);
}