package com.moment.core.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import reactor.util.annotation.Nullable;

import java.time.LocalDateTime;

public class CardViewRequestDTO {

    @Schema(description = "녹음본 저장 요청")
    @Getter
    @Builder
    public static class UploadRecord {

        @Schema(description = "녹음본 장소")
        private final String location;

        @Schema(description = "녹음 시점")
        private final LocalDateTime recordedAt;

        @Schema(description = "날씨")
        private final String weather;

        @Schema(description = "온도")
        private final String temperature;

        @Schema(description = "질문")
        private final String question;
    }

    @Schema(description = "녹음본 수정 요청")
    @Getter
    @Builder
    public static class UpdateRecord {

            @Schema(description = "녹음본 장소")
            private final String location;

            @Schema(description = "날씨")
            private final String weather;

            @Schema(description = "온도")
            private final String temperature;

            @Schema(description = "질문")
            private final String question;

            @Schema(description = "STT")
            private final String stt;
    }
}
