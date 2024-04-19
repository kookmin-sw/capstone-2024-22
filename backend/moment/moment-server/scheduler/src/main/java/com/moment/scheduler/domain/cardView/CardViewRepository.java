package com.moment.scheduler.domain.cardView;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardViewRepository extends JpaRepository<CardView, Long> {
    List<CardView> findAllByTripFile_IdOrderByRecordedAt(Long tripFileId);

    List<CardView> findAllByRecordFileStatusIn(List<String> recordFileStatuses);


    List<CardView> findByTripFile_User_IdAndIsLovedOrderByRecordedAt(Long userId, Boolean isLoved);
}