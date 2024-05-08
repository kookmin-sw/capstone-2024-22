package com.moment.core.service;

import com.moment.core.domain.cardView.CardView;
import com.moment.core.domain.cardView.CardViewRepository;
import com.moment.core.domain.receipt.Receipt;
import com.moment.core.domain.receipt.ReceiptRepository;
import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.trip.TripRepository;
import com.moment.core.domain.tripFile.TripFile;
import com.moment.core.domain.tripFile.TripFileRepository;
import com.moment.core.domain.user.User;
import com.moment.core.domain.user.UserRepository;
import com.moment.core.dto.Pagination;
import com.moment.core.dto.request.ReceiptRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final TripFileRepository tripFileRepository;
    private final TripRepository tripRepository;
    private final CardViewRepository cardViewRepository;

    public Integer getReceiptCount(Long userId) {
        // 유저의 총 영수증 개수 반환
        return receiptRepository.countByTrip_User_Id(userId).intValue();
    }

    public ReceiptRequestDTO.getReceiptAll getAllReceipt(Long userId, Pageable pageable) {
        Page<Receipt> receiptList = receiptRepository.findAllByTrip_User_IdOrderByStDate(userId, pageable);

        Page<ReceiptRequestDTO.getReceipt> receiptDTOPage = receiptList.map(receipt -> mapToReceiptDTO(receipt));

        return ReceiptRequestDTO.getReceiptAll.builder()
                .receiptList(receiptDTOPage.getContent())
                .pagination(Pagination.builder()
                        .totalPages(receiptDTOPage.getTotalPages())
                        .totalElements(receiptDTOPage.getTotalElements())
                        .currentPage(receiptDTOPage.getNumber())
                        .currentElements(receiptDTOPage.getNumberOfElements())
                        .build())
                .build();
    }

    private ReceiptRequestDTO.getReceipt mapToReceiptDTO(Receipt receipt) {
        return ReceiptRequestDTO.getReceipt.builder()
                .id(receipt.getId())
                .tripId(receipt.getTrip().getId())
                .mainDeparture(receipt.getMainDeparture())
                .subDeparture(receipt.getSubDeparture())
                .mainDestination(receipt.getMainDestination())
                .subDestination(receipt.getSubDestination())
                .oneLineMemo(receipt.getOneLineMemo())
                .numOfCard(receipt.getNumOfCard())
                .stDate(receipt.getStDate())
                .edDate(receipt.getEdDate())
                .tripName(receipt.getTrip().getTripName())
                .happy(receipt.getHappy())
                .sad(receipt.getSad())
                .angry(receipt.getAngry())
                .neutral(receipt.getNeutral())
                .disgust(receipt.getDisgust())
                .receiptThemeType(receipt.getReceiptThemeType())
                .createdAt(receipt.getCreatedAt())
                .build();
    }

    public void deleteReceipts(Long userId, ReceiptRequestDTO.deleteReceipts deleteReceipts) {
        List<Receipt> receipts = new ArrayList<>();
        for (ReceiptRequestDTO.deleteReceipt deleteReceipt : deleteReceipts.getReceiptIds()) {
            Receipt receipt = receiptRepository.findById(deleteReceipt.getReceiptId()).orElseThrow(() -> new IllegalArgumentException("해당 영수증이 존재하지 않습니다."));
            validateUserWithReceipt(userId, deleteReceipt.getReceiptId());
            receipts.add(receipt);
        }
        receiptRepository.deleteAll(receipts);
    }

    // 해당 여행의 카드뷰 개수 세기
    public Map<String, Float> getCardViewCount(Trip trip) {
        List<TripFile> tripFiles = tripFileRepository.findAllByTrip(trip);
        Map<String, Float> emotionMap = new HashMap<>();
        Float happy = 0f;
        Float sad = 0f;
        Float angry = 0f;
        Float neutral = 0f;
        Float disgust = 0f;
        emotionMap.put("happy", 0f);
        emotionMap.put("sad", 0f);
        emotionMap.put("angry", 0f);
        emotionMap.put("neutral", 0f);
        emotionMap.put("disgust", 0f);
        emotionMap.put("total", 0f);
        int cardViewCount = 0;
        for (TripFile tripFile : tripFiles) {
            List<CardView> cardViews = cardViewRepository.findAllByTripFile(tripFile);
            cardViewCount += cardViews.size();
            for (CardView cardView : cardViews) {
                happy += cardView.getHappy();
                sad += cardView.getSad();
                angry += cardView.getAngry();
                disgust += cardView.getDisgust();
                neutral += cardView.getNeutral();
            }
        }
        emotionMap.put("total", (float) cardViewCount);
        return emotionMap;
    }

    public void createReceipt(Long userId, ReceiptRequestDTO.createReceipt createReceipt) {
        // 유저가 해당 여행에 접근 권한이 있는지 확인
        Trip trip = tripRepository.findById(createReceipt.getTripId()).orElseThrow(() -> new IllegalArgumentException("해당 여행이 존재하지 않습니다."));
        User user = trip.getUser();
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("해당 여행에 접근 권한이 없습니다.");
        }

        // 해당 여행이 끝났는지, 해당 여행에 분석중인 카드뷰가 남아있는지 확인
        if (!trip.getEndDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("여행이 끝나지 않았습니다.");
        }
        if (trip.getAnalyzingCount() != 0) {
            throw new IllegalArgumentException("분석중인 카드뷰가 남아있습니다.");
        }

        // 해당 여행의 카드뷰 개수 세기
        Map<String, Float> map = getCardViewCount(trip);
        int cardViewCount = map.get("total").intValue();

        // 감정 별 통계
        float happy = map.get("happy");
        float sad = map.get("sad");
        float angry = map.get("angry");
        float neutral = map.get("neutral");
        float disgust = map.get("disgust");
        float total = happy + sad + angry + neutral + disgust;

        happy = (Float.isNaN(happy / total)) ? (float) 0.0 : happy / total;
        sad = (Float.isNaN(sad / total)) ? (float) 0.0 : sad / total;
        angry = (Float.isNaN(angry / total)) ? (float) 0.0 : angry / total;
        neutral = (Float.isNaN(neutral / total)) ? (float) 0.0 : neutral / total;
        disgust = (Float.isNaN(disgust / total)) ? (float) 0.0 : disgust / total;

        // 영수증 생성
        Receipt receipt = Receipt.builder()
                .trip(trip)
                .mainDeparture(createReceipt.getMainDeparture())
                .subDeparture(createReceipt.getSubDeparture())
                .mainDestination(createReceipt.getMainDestination())
                .subDestination(createReceipt.getSubDestination())
                .oneLineMemo(createReceipt.getOneLineMemo())
                .numOfCard(cardViewCount)
                .stDate(trip.getStartDate())
                .edDate(trip.getEndDate())
                .tripName(trip.getTripName())
                .happy(happy)
                .sad(sad)
                .angry(angry)
                .neutral(neutral)
                .disgust(disgust)
                .receiptThemeType(createReceipt.getReceiptThemeType())
                .build();

        receiptRepository.save(receipt);
    }

    public void updateReceipt(Long userId, ReceiptRequestDTO.updateReceipt updateReceipt) {
        Receipt receipt = receiptRepository.findById(updateReceipt.getId()).orElseThrow(() -> new IllegalArgumentException("해당 영수증이 존재하지 않습니다."));
        receipt.setMainDeparture(updateReceipt.getMainDeparture());
        receipt.setSubDeparture(updateReceipt.getSubDeparture());
        receipt.setMainDestination(updateReceipt.getMainDestination());
        receipt.setSubDestination(updateReceipt.getSubDestination());
        receipt.setOneLineMemo(updateReceipt.getOneLineMemo());
        receipt.setReceiptThemeType(updateReceipt.getReceiptThemeType());

        receiptRepository.save(receipt);
    }

    public void validateUserWithReceipt(Long userId, Long id) {
        Receipt receipt = receiptRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 영수증이 존재하지 않습니다."));
        Trip trip = receipt.getTrip();
        User user = trip.getUser();
        if (!user.getId().equals(userId)) {
            throw new IllegalArgumentException("해당 영수증을 수정할 권한이 없습니다.");
        }
    }
}
