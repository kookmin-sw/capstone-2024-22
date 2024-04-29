package com.moment.core.service;

import com.moment.core.domain.cardView.CardView;
import com.moment.core.domain.cardView.CardViewRepository;
import com.moment.core.domain.imageFile.ImageFile;
import com.moment.core.domain.imageFile.ImageFileRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;
    private final S3Service s3Service;
    private final CardViewRepository cardViewRepository;

    @Transactional
    public void deleteAll(CardView cardView) {
        List<ImageFile> imageFiles = imageFileRepository.findAllByCardView(cardView);
        for (ImageFile imageFile : imageFiles) {
            s3Service.deleteFile(imageFile.getFileName());
        }
        imageFiles.forEach(imageFileRepository::delete);
    }

    @Transactional
    public void uploadAll(List<MultipartFile> imageFiles, Long cardViewId, Long userId) throws IOException {
        for (MultipartFile imageFile : imageFiles) {
            CardView cv = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("해당 카드뷰가 없습니다."));
            String filename = UUID.randomUUID().toString();
            String url = s3Service.uploadFile(imageFile, userId, filename, false);
            ImageFile image = ImageFile.builder()
                    .fileUrl(url)
                    .fileName(filename)
                    .cardView(cv)
                    .build();
            imageFileRepository.save(image);
        }

    }

}
