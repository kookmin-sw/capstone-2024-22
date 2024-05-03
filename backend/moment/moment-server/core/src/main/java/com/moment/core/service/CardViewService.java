package com.moment.core.service;

import com.moment.core.domain.cardView.CardView;
import com.moment.core.domain.cardView.CardViewRepository;
import com.moment.core.domain.tripFile.TripFile;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.request.CardViewRequestDTO;
import com.moment.core.dto.response.CardViewResponseDTO;
import com.moment.core.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class CardViewService {
    private final CardViewRepository cardViewRepository;
    private final UserRepository userRepository;
    private final TripService tripService;
    private final TripFileService tripFileService;
    private final ImageFileService imageFileService;
    private final S3Service s3Service;


    @Value("${file.path}")
    private String filePath;

    @Autowired
    public CardViewService(CardViewRepository cardViewRepository, UserRepository userRepository, TripService tripService, TripFileService tripFileService, ImageFileService imageFileService, S3Service s3Service) {
        this.cardViewRepository = cardViewRepository;
        this.userRepository = userRepository;
        this.tripService = tripService;
        this.tripFileService = tripFileService;
        this.imageFileService = imageFileService;
        this.s3Service = s3Service;
    }


    // 녹음본과 데이터를 받아서 저장
    // 여행 파일이 존재한다면 해당 여행파일에 등록
    // 없다면 새로운 여행파일을 생성, untitled에 저장
    @Transactional
    public CardViewResponseDTO.GetCardView uploadRecord(CardViewRequestDTO.UploadRecord uploadRecord, MultipartFile recordFile, Long userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("해당 유저가 없습니다."));

        // 여행파일이 존재하는지 확인 없다면 untitled 여행안에 생성
        Optional<TripFile> tf = tripFileService.findByUserAndYearDate(user, uploadRecord.getRecordedAt().toLocalDate());
        TripFile tripFile;
        tripFile = tf.orElseGet(() -> TripFile.builder()
                .trip(tripService.getUntitledTrip(user))
                .user(user)
                .yearDate(uploadRecord.getRecordedAt().toLocalDate())
                .analyzingCount(0)
                .build());

        // recordFile의 길이 받아오기
        long length = recordFile.getSize();

        // 파일 이름 생성
        String fileName = createFileName(recordFile.getOriginalFilename());

        // 로컬 저장소에 파일 저장
        String url = s3Service.uploadToS3(recordFile, userId, fileName, true);

        CardView cardView = CardView.builder()
                .recordedAt(uploadRecord.getRecordedAt())
                .happy(null)
                .angry(null)
                .sad(null)
                .neutral(null)
                .disgust(null)
                .stt(null)
                .isLoved(false)
                .question(uploadRecord.getQuestion())
                .location(uploadRecord.getLocation())
                .recordFileStatus("WAIT")
                .recordFileLength(length)
                .recordFileUrl(url)
                .recordFileName(fileName)
//                .user(user)
                .tripFile(tripFile)
                .weather(uploadRecord.getWeather())
                .temperature(uploadRecord.getTemperature())
                .build();

        // tripFile, trip의 analyzingCount 증가
        tripFileService.increaseAnalyzingCount(tripFile);
        tripService.increaseAnalyzingCount(tripFile.getTrip());
        List<String> imageUrls = new ArrayList<>();
        return CardViewResponseDTO.GetCardView.fromEntity(cardViewRepository.save(cardView), imageUrls);
    }

    private String createFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = LocalDateTime.now().toString();
        return uuid + "." + ext;
    }

    // 확장자명 구하기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // fullPath 만들기
    private String getFullPath(String filename) {
        return filePath + filename;
    }

    public CardViewResponseDTO.GetAllCardView getAllCardView(Long userId, Long tripFileId) {
        List<CardViewResponseDTO.GetCardView> rtnList = new ArrayList<>();
        List<CardView> cardViews = cardViewRepository.findAllByTripFile_IdOrderByRecordedAt(tripFileId);
        for (CardView cardView : cardViews) {
            List<String> imageUrls = imageFileService.getImageUrls(cardView);
            rtnList.add(CardViewResponseDTO.GetCardView.fromEntity(cardView, imageUrls));
        }
        return CardViewResponseDTO.GetAllCardView.builder().cardViews(rtnList).build();
    }

    public void updateRecord(Long cardViewId, CardViewRequestDTO.UpdateRecord updateRecord) {
        CardView cardView = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드뷰입니다."));

        // update
        if (updateRecord.getLocation() != null)
            cardView.setLocation(updateRecord.getLocation());
        if (updateRecord.getStt() != null)
            cardView.setStt(updateRecord.getStt());
        if (updateRecord.getQuestion() != null)
            cardView.setQuestion(updateRecord.getQuestion());
        if (updateRecord.getWeather() != null)
            cardView.setWeather(updateRecord.getWeather());
        if (updateRecord.getTemperature() != null)
            cardView.setTemperature(updateRecord.getTemperature());

        cardViewRepository.save(cardView);
    }

    @Transactional
    public void deleteRecord(Long cardViewId) {
        CardView cardView = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드뷰입니다."));
        // cardView에 엮인 사진들 먼저 삭제
        String userId = cardView.getTripFile().getUser().getId().toString();
        imageFileService.deleteAll(cardView, userId);
        s3Service.deleteFile(cardView.getRecordFileName(), userId);
        boolean isAnalyzed = cardView.getRecordFileStatus().equals("WAIT");
        // 만약 tripfile의 Trip이 untitled일 경우
        //     만약 tripfile의 크기가 1이라면 tripFile과 cardView 전부 삭제, untitledTrip의 analyzingCount 감소
        TripFile tripFile = cardView.getTripFile();
        if (tripFile.getTrip().getIsNotTitled()) {
            if (tripFileService.getCardViewCount(tripFile) == 1) {
                cardViewRepository.delete(cardView);
                tripFileService.delete(cardView.getTripFile());
                if (isAnalyzed)
                    tripService.decreaseAnalyzingCount(cardView.getTripFile().getTrip());
            }
        }
        // 만약 tripfile의 trip이 untitled가 아닐경우
        // cardView만 삭제, tripFile의 analyzingCount 감소
        // trip의 analyzingCount 감소
        else {
            cardViewRepository.delete(cardView);
            if (isAnalyzed){
                tripFileService.decreaseAnalyzingCount(cardView.getTripFile());
                tripService.decreaseAnalyzingCount(cardView.getTripFile().getTrip());
            }
        }
    }

    public void likeCardView(Long cardViewId) {
        CardView cardView = cardViewRepository.findById(cardViewId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카드뷰입니다."));
        cardView.setIsLoved(!cardView.getIsLoved());
        cardViewRepository.save(cardView);
    }

    public CardViewResponseDTO.GetAllCardView getLikeCardView(Long userId) {
        List<CardViewResponseDTO.GetCardView> rtnList = new ArrayList<>();
        List<CardView> cardViews = cardViewRepository.findByTripFile_User_IdAndIsLovedOrderByRecordedAt(userId, true);
        for (CardView cardView : cardViews) {
            List<String> imageUrls = imageFileService.getImageUrls(cardView);
            rtnList.add(CardViewResponseDTO.GetCardView.fromEntity(cardView, imageUrls));
        }
        return CardViewResponseDTO.GetAllCardView.builder().cardViews(rtnList).build();
    }
}
