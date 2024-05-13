package com.moment.scheduler.config;

import com.moment.scheduler.domain.user.UserRepository;
import com.moment.scheduler.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {
    private final NotiService notiService;
    private final UserRepository userRepository;

    // 매일 오후 1시에 알림 발송
//    @Scheduled(cron = "0 0 13 * * *")
    @Scheduled(cron = "22 06 * * * *")
    public void run() {
        log.info("Noti Scheduler is running");
        try {
            List<String> questions = Arrays.asList("오늘의 일정은 어떠셨나요?", "오늘 날씨는 어때요?", "맛있는 점심 먹었나요?");
            String question = questions.get((int) (Math.random() * questions.size()));
            userRepository.findAll().forEach(user -> notiService.sendQuestionNoti(user, question));
        } catch (Exception e) {
            log.error("Failed to send notification");
        }
    }
}