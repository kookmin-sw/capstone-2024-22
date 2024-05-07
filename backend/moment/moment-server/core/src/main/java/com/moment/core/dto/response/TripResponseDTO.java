package com.moment.core.dto.response;

import com.moment.core.domain.trip.Trip;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class TripResponseDTO {

    @Schema(description = "여행 반환")
    @Getter
    @Builder
    public static class GetTrip {
        @Schema(description = "여행 ID")
        private final Long id;

        @Schema(description = "유저 email")
        private final String email;

        @Schema(description = "출발일")
        private final LocalDate startDate;

        @Schema(description = "도착일")
        private final LocalDate endDate;

        @Schema(description = "분석 중 파일 개수")
        private final Integer analyzingCount;

        @Schema(description = "여행 이름")
        private final String tripName;

        // fromEntity
        public static GetTrip fromEntity(Trip trip) {
            return GetTrip.builder()
                    .id(trip.getId())
                    .email(trip.getUser().getEmail())
                    .startDate(trip.getStartDate())
                    .endDate(trip.getEndDate())
                    .analyzingCount(trip.getAnalyzingCount())
                    .tripName(trip.getTripName())
                    .build();
        }
    }

    @Schema(description = "전체 여행 반환")
    @Getter
    @Builder
    public static class GetAllTrip {
        private final List<GetTrip> trips;
    }

    @Getter
    @Builder
    public static class GetTripSpec {
        private final Long id;

        private final String email;

        private final LocalDate startDate;

        private final LocalDate endDate;

        private final Integer analyzingCount;

        private final String tripName;

        private final int numOfCard;

        private final Float happy;

        private final Float sad;

        private final Float angry;

        private final Float neutral;

        private final Float disgust;

        public static GetTripSpec fromEntity(Trip trip, int numOfCard, Float happy, Float sad, Float angry, Float neutral, Float disgust) {
            return GetTripSpec.builder()
                    .id(trip.getId())
                    .email(trip.getUser().getEmail())
                    .startDate(trip.getStartDate())
                    .endDate(trip.getEndDate())
                    .analyzingCount(trip.getAnalyzingCount())
                    .tripName(trip.getTripName())
                    .numOfCard(numOfCard)
                    .happy(happy)
                    .sad(sad)
                    .angry(angry)
                    .neutral(neutral)
                    .disgust(disgust)
                    .build();
        }
    }
}
