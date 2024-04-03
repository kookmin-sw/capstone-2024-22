package com.moment.scheduler.controller;

import com.moment.scheduler.service.AiService;
import com.moment.scheduler.service.CardViewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/sc/scheduler")
public class SchedulerController {
    private final CardViewService cardViewService;

    @PatchMapping("/run")
    public void runScheduler() {
        cardViewService.getIncompleteCardViews();
    }
}
