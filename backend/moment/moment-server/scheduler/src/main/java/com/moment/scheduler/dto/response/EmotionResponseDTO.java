package com.moment.scheduler.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class EmotionResponseDTO {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class GetEmotionList {
        private final Float happy;
        private final Float sad;
        private final Float angry;
        private final Float neutral;
    }


}
