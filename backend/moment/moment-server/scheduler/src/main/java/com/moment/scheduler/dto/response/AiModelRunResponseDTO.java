package com.moment.scheduler.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class AiModelRunResponseDTO {

    @Getter
    @Builder
    public static class RunModel {
        private final String status;
        private final EmotionResponseDTO.GetEmotionList emotions;
        private final String text;
        private final List<String> log;
        private final String error;
    }
}
