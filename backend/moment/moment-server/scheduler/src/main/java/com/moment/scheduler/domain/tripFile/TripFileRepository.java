package com.moment.scheduler.domain.tripFile;


import com.moment.scheduler.domain.cardView.CardView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TripFileRepository extends JpaRepository<TripFile, Long> {




}