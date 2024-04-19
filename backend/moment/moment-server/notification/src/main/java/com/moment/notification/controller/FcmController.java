package com.moment.notification.controller;

import com.moment.notification.DTO.FcmSendDto;
import com.moment.notification.service.FcmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/noti")
public class FcmController {
    private final FcmService fcmService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("test", HttpStatus.OK);
    }

    @PostMapping("/fcm/send")
    public ResponseEntity<Integer> pushMessage(@RequestBody @Validated FcmSendDto fcmSendDto) throws IOException {
        log.debug("[+] 푸시 메시지를 전송합니다. ");
        int result = fcmService.sendMessageTo(fcmSendDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}