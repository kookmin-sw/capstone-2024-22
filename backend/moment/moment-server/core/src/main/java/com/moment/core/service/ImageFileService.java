package com.moment.core.service;

import com.moment.core.domain.cardView.CardView;
import com.moment.core.domain.imageFile.ImageFile;
import com.moment.core.domain.imageFile.ImageFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;

    @Transactional
    public void deleteAll(CardView cardView) {
        List<ImageFile> imageFiles = imageFileRepository.findAllByCardView(cardView);
        imageFiles.forEach(imageFileRepository::delete);
    }

}
