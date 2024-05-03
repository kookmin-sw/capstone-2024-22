package com.moment.core.dto.response;

import com.moment.core.domain.tripFile.TripFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class TripFileResponseDTO {

    @Schema(description = "여행 파일 반환")
    @Getter
    @Builder
    public static class GetTripFile{
        @Schema(description = "파일 ID")
        private final Long id;

        @Schema(description = "여행 ID")
        private final Long tripId;

        @Schema(description = "유저 email")
        private final String email;

        @Schema(description = "연월일")
        private LocalDate yearDate;

        @Schema(description = "분석 중 파일 개수")
        private Integer analyzingCount;

        private Integer totalCount;

        // fromEntity
        public static GetTripFile fromEntity(TripFile tripFile, Integer totalCount) {
            return GetTripFile.builder()
                    .id(tripFile.getId())
                    .tripId(tripFile.getTrip().getId())
                    .email(tripFile.getUser().getEmail())
                    .yearDate(tripFile.getYearDate())
                    .analyzingCount(tripFile.getAnalyzingCount())
                    .totalCount(totalCount)
                    .build();
        }
    }

    @Schema(description = "전체 여행 파일 반환")
    @Getter
    @Builder
    public static class GetAllTripFile {
        private final List<GetTripFile> tripFiles;
    }
}
