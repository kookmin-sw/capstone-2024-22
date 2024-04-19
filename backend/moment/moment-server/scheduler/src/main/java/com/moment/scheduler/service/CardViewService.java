package com.moment.scheduler.service;


import com.moment.scheduler.domain.cardView.CardView;
import com.moment.scheduler.domain.cardView.CardViewRepository;
import com.moment.scheduler.domain.trip.Trip;
import com.moment.scheduler.domain.trip.TripRepository;
import com.moment.scheduler.domain.tripFile.TripFile;
import com.moment.scheduler.domain.tripFile.TripFileRepository;
import com.moment.scheduler.domain.user.UserRepository;

import com.moment.scheduler.dto.response.AiModelRunResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class CardViewService {
    private final CardViewRepository cardViewRepository;
    private final AiService aiService;
    private final TripRepository tripRepository;
    private final TripFileRepository tripFileRepository;

    @Async
    public void getIncompleteCardViews() {
        List<CardView> cards = cardViewRepository.findAllByRecordFileStatusIn(List.of("WAIT", "FAIL"));
        for (CardView card : cards) {
            AiModelRunResponseDTO.RunModel ret = aiService.runAi(card.getRecordFileName());
            log.info("ret.status : " + ret.getStatus());
            log.info("ret.text : " + ret.getText());
            log.info("ret.happy : " + ret.getEmotions().getHappy());
            log.info("ret.sad : " + ret.getEmotions().getSad());
            log.info("ret.angry : " + ret.getEmotions().getAngry());
            log.info("ret.neutral : " + ret.getEmotions().getNeutral());
            log.info("ret.error : " + ret.getError());
            log.info("ret.file_name : " + ret.getFile_name());
            log.info("ret.file_path : " + ret.getFile_path());
            if (Objects.equals(ret.getStatus(), "wait")){
                card.setRecordFileStatus("DONE");
                card.setStt(ret.getText());
                card.setHappy(ret.getEmotions().getHappy());
                card.setSad(ret.getEmotions().getSad());
                card.setAngry(ret.getEmotions().getAngry());
                card.setNeutral(ret.getEmotions().getNeutral());

                TripFile tripFile = card.getTripFile();
                tripFile.setAnalyzingCount(tripFile.getAnalyzingCount() - 1);
                Trip trip = tripFile.getTrip();
                trip.setAnalyzingCount(trip.getAnalyzingCount() - 1);
                tripRepository.save(trip);
                tripFileRepository.save(tripFile);
                cardViewRepository.save(card);
            }
            else {
                card.setRecordFileStatus("FAIL");
                cardViewRepository.save(card);
            }

        }
    }



}
