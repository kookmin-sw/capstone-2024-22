package com.moment.scheduler.controller;

import com.moment.scheduler.service.AwsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/sc")
public class RootController {
    private final AwsService awsService;
    private final Environment env;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        // 서버 아이디와 포트 출력
        log.info("health-check called" );
        return ResponseEntity.ok("I'm alive : ai");
    }

    @PostMapping("/turn-on/ec2")
    public ResponseEntity<String> turnOn() {
        awsService.turnOnOrOff("moment-ai-t4", true);
        return ResponseEntity.ok("EC2 is turned on");
    }

    @GetMapping("/is-on/ec2")
    public ResponseEntity<String> isOn() {
        return ResponseEntity.ok(awsService.isEc2Running() ? "EC2 is on" : "EC2 is off");
    }

}
