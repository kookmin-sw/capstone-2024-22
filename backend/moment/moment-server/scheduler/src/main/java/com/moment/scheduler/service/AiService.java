package com.moment.scheduler.service;

import com.moment.scheduler.client.AiClient;
import com.moment.scheduler.dto.request.AiModelRunRequestDTO;
import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.EmotionResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AiService {
    private final AiClient aiClient;
    private final static String file_path = "";

    public AiModelRunResponseDTO.RunModel runAi(String fileName) {
        return aiClient.runAi(
                AiModelRunRequestDTO.RunModel.builder()
                        .file_name(fileName)
                        .file_path(file_path)
                        .build()
        );
    }


}
