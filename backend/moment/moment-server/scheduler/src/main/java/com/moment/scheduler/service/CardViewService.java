package com.moment.scheduler.service;


import com.moment.scheduler.domain.cardView.CardView;
import com.moment.scheduler.domain.cardView.CardViewRepository;
import com.moment.scheduler.domain.trip.Trip;
import com.moment.scheduler.domain.trip.TripRepository;
import com.moment.scheduler.domain.tripFile.TripFile;
import com.moment.scheduler.domain.tripFile.TripFileRepository;
import com.moment.scheduler.domain.user.User;

import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import com.moment.scheduler.dto.response.SchedulerResponseDTO;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Thread.sleep;


@Service
@RequiredArgsConstructor
@Slf4j
public class CardViewService {
    private final CardViewRepository cardViewRepository;
    private final AiService aiService;
    private final TripRepository tripRepository;
    private final TripFileRepository tripFileRepository;
    private final AwsService awsService;
    private final NotiService notiService;
    private final EntityManager em;

    public SchedulerResponseDTO.AIModelRunResponseDTO getIncompleteCardViews() throws InterruptedException {
        // ec2가 켜져있는지 확인
//        if (awsService.isEc2Running()){
//            throw new RuntimeException("EC2 is already running");
//        }
//        awsService.turnOnOrOff("moment-ai-t4", true);
        log.info("EC2 trying to turn on");
        while (!awsService.isEc2Running()){
            log.info("sleep");
            sleep(9000);
        }
        // 분석 완료 카드뷰 개수를 유저별로 저장
        Map<User, Integer> userCardViewCount = new HashMap<>();

//        sleep(31000);
        // 경과 시간 체크를 위한 시작 시간
        long startTime = System.currentTimeMillis();
        List<CardView> cards = cardViewRepository.findAllByRecordFileStatusIn(List.of("WAIT"));
        // "FAIL" 잠깐 뻄
        Integer totalCardNum = cards.size();
        Integer failRecordNum = 0;
        Integer successRecordNum = 0;

        log.info("cards.size : " + cards.size());
        for (CardView card : cards) {
            Trip ptrip = card.getTripFile().getTrip();
            User user = ptrip.getUser();
            AiModelRunResponseDTO.RunModel ret = aiService.runAi(card.getRecordFileName(), user.getId());
            if (Objects.equals(ret.getStatus(), "200")){
                log.info("ret.status : " + ret.getStatus());
                log.info("ret.text : " + ret.getText());
                log.info("ret.happy : " + ret.getEmotions().getHappy());
                log.info("ret.sad : " + ret.getEmotions().getSad());
                log.info("ret.angry : " + ret.getEmotions().getAngry());
                log.info("ret.neutral : " + ret.getEmotions().getNeutral());
                log.info("ret.disgust : " + ret.getEmotions().getDisgust());
                log.info("ret.error : " + ret.getError());
                log.info("ret.file_name : " + ret.getFile_name());
                log.info("ret.file_path : " + ret.getFile_path());
                card.setRecordFileStatus("DONE");
                card.setStt(ret.getText());
                card.setHappy(ret.getEmotions().getHappy());
                card.setSad(ret.getEmotions().getSad());
                card.setAngry(ret.getEmotions().getAngry());
                card.setNeutral(ret.getEmotions().getNeutral());
                card.setDisgust(ret.getEmotions().getDisgust());
                cardViewRepository.save(card);
                em.clear();
                // 분석도중 레이스컨디션 때문에 다시 여행을 불러오기
                TripFile cTripFile = tripFileRepository.findById(card.getTripFile().getId()).get();
                cTripFile.setAnalyzingCount(cTripFile.getAnalyzingCount() - 1);
                Trip trip = cTripFile.getTrip();
                trip.setAnalyzingCount(trip.getAnalyzingCount() - 1);

                tripRepository.save(trip);
                log.info("tripfiles tripid : " + cTripFile.getTrip().getId());
                tripFileRepository.save(cTripFile);

                successRecordNum++;
                // 유저가 없는경우 map에 추가 있으면 카운트 추가
                if (!userCardViewCount.containsKey(user)){
                    userCardViewCount.put(user, 1);
                }
                else {
                    userCardViewCount.put(user, userCardViewCount.get(user) + 1);
                }
            }
            else {
                log.info("AI model run failed");
                log.info("status : " + ret.getStatus());
                log.info("error : " + ret.getError());
                card.setRecordFileStatus("FAIL");
                cardViewRepository.save(card);
                failRecordNum++;
            }

        }
        long endTime = System.currentTimeMillis();
        log.info("AI model run time : " + (endTime - startTime) + "ms");
//        awsService.turnOnOrOff("moment-ai-t4", false);

        // 분석한만큼 유저에게 알림을 전송
        for (User user : userCardViewCount.keySet()){
            notiService.sendFinishNoti(user, userCardViewCount.get(user));
        }

//        while (awsService.isEc2Running()){
//            sleep(5000);
//        }
        return SchedulerResponseDTO.AIModelRunResponseDTO.builder()
                .failRecordNum(failRecordNum)
                .successRecordNum(successRecordNum)
                .totalRecordNum(totalCardNum)
                .totalElapsedTime(endTime - startTime)
                .build();
    }



}
