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
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;
    private final S3Service s3Service;
    private final CardViewRepository cardViewRepository;
    private final UserService userService;

    @Transactional
    public void deleteAll(CardView cardView, String userId) {
        List<ImageFile> imageFiles = imageFileRepository.findAllByCardView(cardView);
        for (ImageFile imageFile : imageFiles) {
            s3Service.deleteFile(imageFile.getFileName(), userId);
        }
        imageFiles.forEach(imageFileRepository::delete);
    }

    @Transactional
    public void uploadAll(List<MultipartFile> imageFiles, Long cardViewId, Long userId) throws IOException {
        for (MultipartFile imageFile : imageFiles) {
            CardView cv = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("해당 카드뷰가 없습니다."));
            String filename = UUID.randomUUID().toString();
            String extension = Objects.requireNonNull(imageFile.getOriginalFilename()).substring(imageFile.getOriginalFilename().lastIndexOf("."));
            String url = s3Service.uploadFile(imageFile, userId, filename + extension, false);
            ImageFile image = ImageFile.builder()
                    .fileUrl(url)
                    .fileName(filename)
                    .cardView(cv)
                    .build();
            imageFileRepository.save(image);
        }

    }

    @Transactional
    public void deleteImages(List<Long> images, Long userId) {
        for (Long imageId : images) {
            ImageFile image = imageFileRepository.findById(imageId).orElseThrow(() -> new IllegalArgumentException("해당 이미지가 없습니다."));
            CardView cardView = image.getCardView();
            userService.validateUserWithCardView(userId, cardView.getId());
            s3Service.deleteFile(image.getFileName(), userId.toString());
            imageFileRepository.delete(image);
        }
    }

    public List<String> getImageUrls(CardView cardView) {
        List<ImageFile> imageFiles = imageFileRepository.findAllByCardView(cardView);
        return imageFiles.stream().map(ImageFile::getFileUrl).toList();
    }
}
