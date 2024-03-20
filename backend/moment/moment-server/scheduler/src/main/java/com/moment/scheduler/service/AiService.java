package com.moment.scheduler.service;

import com.moment.scheduler.client.AiClient;
import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.EmotionResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AiService {
    private final AiClient aiClient;

    public AiModelRunResponseDTO.RunModel runAi(String fileName) {
        return aiClient.runAi(fileName);
    }
}
