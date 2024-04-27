package com.moment.scheduler.controller;

import com.moment.scheduler.common.APIResponse;
import com.moment.scheduler.common.code.SuccessCode;
import com.moment.scheduler.dto.response.SchedulerResponseDTO;
import com.moment.scheduler.service.AiService;
import com.moment.scheduler.service.CardViewService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/sc/scheduler")
public class SchedulerController {
    private final CardViewService cardViewService;

    @PatchMapping("/run")
    public ResponseEntity<APIResponse<SchedulerResponseDTO.AIModelRunResponseDTO>> runScheduler() throws InterruptedException {
        return ResponseEntity.ok(APIResponse.of(SuccessCode.EXECUTE_SUCCESS, cardViewService.getIncompleteCardViews()));
    }
}
