package com.moment.scheduler.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class SchedulerResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class AIModelRunResponseDTO {
        private final Integer totalRecordNum;
        private final Integer successRecordNum;
        private final Integer failRecordNum;
        private final Long totalElapsedTime;
    }
}
