package com.moment.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class TripRequestDTO {

    @Schema(description = "여행 저장 요청")
    @Getter
    @Builder
    public static class RegisterTrip {

        @Schema(description = "출발일")
        private LocalDate startDate;

        @Schema(description = "도착일")
        private LocalDate endDate;

        @Schema(description = "여행 이름")
        private String tripName;
    }

    @Schema(description = "여행 수정 요청")
    @Getter
    @Builder
    public static class UpdateTrip {

        @Schema(description = "여행 ID")
        private final Long tripId;

        @Schema(description = "출발일")
        private LocalDate startDate;

        @Schema(description = "도착일")
        private LocalDate endDate;

        @Schema(description = "여행 이름")
        private String tripName;
    }
}
