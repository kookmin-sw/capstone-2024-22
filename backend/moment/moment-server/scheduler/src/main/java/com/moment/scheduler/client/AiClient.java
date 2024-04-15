package com.moment.scheduler.client;

import com.moment.scheduler.dto.request.AiModelRunRequestDTO;
import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.EmotionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ai-service", url = "15.164.139.204:5000")
public interface AiClient {

    /**
     * AI 실행
     * @Param String fileName
     */
    @PostMapping("/ai/run")
    AiModelRunResponseDTO.RunModel runAi(@RequestBody AiModelRunRequestDTO.RunModel request);

}
