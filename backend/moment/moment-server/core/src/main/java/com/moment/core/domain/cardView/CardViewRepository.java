package com.moment.core.domain.cardView;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardViewRepository extends JpaRepository<CardView, Long> {
    List<CardView> findAllByTripFile_Id(Long tripFileId);
}