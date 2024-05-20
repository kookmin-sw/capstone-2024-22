package com.moment.scheduler.client;

import com.moment.scheduler.dto.request.FcmSendDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "noti-service")
public interface NotiClient {

    @PostMapping("/noti/fcm/send")
    ResponseEntity<Integer> pushMessage(FcmSendDto request);
}
