package com.moment.core.domain.imageFile;

import com.moment.core.domain.cardView.CardView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
    List<ImageFile> findAllByCardView_Id(Long cardViewId);

    List<ImageFile> findAllByCardView(CardView cardView);
}