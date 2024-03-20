package com.moment.scheduler.client;

import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.EmotionResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ai-service")
public interface AiClient {

    /**
     * AI 실행
     * @Param String fileName
     */
    @PostMapping("/ai/run")
    AiModelRunResponseDTO.RunModel runAi(@RequestParam String file_name);

}
