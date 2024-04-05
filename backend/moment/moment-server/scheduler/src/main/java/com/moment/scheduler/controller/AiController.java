package com.moment.scheduler.controller;

import com.moment.scheduler.dto.request.AiModelRunRequestDTO;
import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.EmotionResponseDTO;
import com.moment.scheduler.service.AiService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/sc/ai")
public class AiController {

    private final AiService aiService;

    @PostMapping("/run")
    public AiModelRunResponseDTO.RunModel runAi(@RequestBody AiModelRunRequestDTO.RunModel request) {
        return aiService.runAi(request.getFile_name());
    }
}
