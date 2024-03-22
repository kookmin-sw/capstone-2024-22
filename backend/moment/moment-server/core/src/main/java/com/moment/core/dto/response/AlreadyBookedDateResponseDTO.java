package com.moment.core.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public class AlreadyBookedDateResponseDTO {

    @Schema(description = "예약일 반환")
    @Getter
    @Builder
    public static class GetCardView{
        @Schema(description = "예약일")
        private final LocalDate bookedDate;
    }


    @Schema(description = "예약일 전부 반환")
    @Getter
    @Builder
    public static class GetAllCardView {
        @Schema(description = "예약일 리스트")
        private final List<AlreadyBookedDateResponseDTO.GetCardView> cardViews;
    }
}
