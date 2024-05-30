package com.moment.scheduler.service;

import com.moment.scheduler.client.NotiClient;
import com.moment.scheduler.domain.alreadyBookedDate.AlreadyBookedDate;
import com.moment.scheduler.domain.alreadyBookedDate.AlreadyBookedDateRepository;
import com.moment.scheduler.domain.user.User;
import com.moment.scheduler.domain.user.UserRepository;
import com.moment.scheduler.dto.request.FcmSendDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotiService {
    private final UserRepository userRepository;
    private final NotiClient notiClient;
    private final AlreadyBookedDateRepository alreadyBookedDateRepository;

    public boolean validateUser(User user) {
        if (!user.isNotification()){
            log.info("User {} is not allowed to receive notification", user.getId());
            return false;
        }
        if (user.getFirebaseToken() == null){
            log.info("User {} has no firebase token", user.getId());
            return false;
        }
        return true;
    }

    @Async
    public void sendFinishNoti(User user, Integer integer) {
        if (!validateUser(user)){
            return;
        }

        try {
            ResponseEntity<Integer> ret = notiClient.pushMessage(
                    FcmSendDto.builder()
                            .token(user.getFirebaseToken())
                            .title("moment")
                            .body(integer + "개의 분석이 끝났어요!")
                            .build()
            );
            log.info("Notification sent to user {} with response {}", user.getId(), ret);
        } catch (Exception e){
            log.error("Failed to send notification to user {}", user.getId());
        }
    }

    @Async
    public void sendQuestionNoti(User user, String question) {
        if (!validateUser(user)){
            return;
        }
        // 해당 유저가 여행중인지 확인
        List<AlreadyBookedDate> alreadyBookedDates = alreadyBookedDateRepository.findAllByUser(user);
        // 오늘날짜가 여행중인 날짜에 포함되어있는지 확인
        boolean isTraveling = false;
        for (AlreadyBookedDate alreadyBookedDate : alreadyBookedDates) {
            if (alreadyBookedDate.getYearDate().equals(java.time.LocalDate.now())){
                isTraveling = true;
                break;
            }
        }
        if (!isTraveling){
            log.info("User {} is not traveling today", user.getId());
            int rand = (int) (Math.random() * 10);
            if (rand % 2 == 0){
                    log.info("Sending random notification to user {}", user.getId());
                try {
                    ResponseEntity<Integer> ret = notiClient.pushMessage(
                            FcmSendDto.builder()
                                    .token(user.getFirebaseToken())
                                    .title("moment")
                                    .body("문득 갑자기 떠나보는건 어때요?")
                                    .build()
                    );
                    log.info("Notification sent to user {} with response {}", user.getId(), ret);
                } catch (Exception e){
                    log.error("Failed to send notification to user {}", user.getId());
                }
            }
        }else{
            try {
                ResponseEntity<Integer> ret = notiClient.pushMessage(
                        FcmSendDto.builder()
                                .token(user.getFirebaseToken())
                                .title("moment")
                                .body(question)
                                .build()
                );
                log.info("Notification sent to user {} with response {}", user.getId(), ret);
            } catch (Exception e){
                log.error("Failed to send notification to user {}", user.getId());
            }
        }
    }

}
