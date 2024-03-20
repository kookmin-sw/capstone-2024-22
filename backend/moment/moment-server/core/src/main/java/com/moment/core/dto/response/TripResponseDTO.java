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

        @Schema(description = "유저 ID")
        private final String userLoginId;

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
                    .userLoginId(trip.getUser().getLoginId())
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
}
