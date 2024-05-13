package com.moment.scheduler.dto.response;

import com.moment.scheduler.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

public class SchedulerResponseDTO {
    @Getter
    @Setter
    @Builder
    public static class AIModelRunResponseDTO {
        private final Integer totalRecordNum;
        private final Integer successRecordNum;
        private final Integer failRecordNum;
        private final Long totalElapsedTime;
        private final Map<User, Integer> userCardViewCount;
    }
}
