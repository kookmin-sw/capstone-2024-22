package com.moment.core.dto.response;

import com.moment.core.domain.cardView.CardView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CardViewResponseDTO {

    @Schema(description = "녹음본 저장 반환")
    @Getter
    @Builder
    public static class GetCardView {

        @Schema(description = "cardView ID")
        private final Long Id;

        @Schema(description = "tripFile ID")
        private final Long tripFileId;

//        @Schema(description = "user email")
//        private final String email;

        @Schema(description = "녹음한 시점")
        private final LocalDateTime recordedAt;

        @Schema(description = "파일이름")
        private final String recordFileName;

        @Schema(description = "파일 저장 위치")
        private final String recordFileUrl;

        @Schema(description = "위치")
        private final String location;

        @Schema(description = "파일길이")
        private final Long recordFileLength;

        @Schema(description = "날씨")
        private final String weather;

        @Schema(description = "온도")
        private final String temperature;

        @Schema(description = "STT")
        private final String stt;

        @Schema(description = "기쁨")
        private final Float happy;

        @Schema(description = "슬픔")
        private final Float sad;

        @Schema(description = "화남")
        private final Float angry;

        @Schema(description = "중립")
        private final Float neutral;

        @Schema(description = "질문")
        private final String question;

        @Schema(description = "즐겨찾기")
        private final boolean isLoved;

        @Schema(description = "분석 상태")
        private final String recordFileStatus;

        // fromEntity
        public static GetCardView fromEntity(CardView cardView) {
            return GetCardView.builder()
                    .Id(cardView.getId())
                    .tripFileId(cardView.getTripFile().getId())
//                    .email(cardView.getUser().getEmail())
                    .recordedAt(cardView.getRecordedAt())
                    .recordFileName(cardView.getRecordFileName())
                    .recordFileUrl(cardView.getRecordFileUrl())
                    .location(cardView.getLocation())
                    .recordFileLength(cardView.getRecordFileLength())
                    .weather(cardView.getWeather())
                    .temperature(cardView.getTemperature())
                    .stt(cardView.getStt())
                    .happy(cardView.getHappy())
                    .sad(cardView.getSad())
                    .angry(cardView.getAngry())
                    .neutral(cardView.getNeutral())
                    .question(cardView.getQuestion())
                    .isLoved(cardView.getIsLoved())
                    .recordFileStatus(cardView.getRecordFileStatus())
                    .build();
        }
    }

    @Schema(description = "카드뷰 전부 반환")
    @Getter
    @Builder
    public static class GetAllCardView {
        @Schema(description = "카드뷰 리스트")
        private final List<GetCardView> cardViews;
    }
}
