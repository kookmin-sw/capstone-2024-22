package com.moment.mail.controller;

import com.moment.mail.dto.EmailMessage;
import com.moment.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @PostMapping()
    public ResponseEntity<String> sendMail(
            @RequestBody EmailMessage emailMessage) {
        return ResponseEntity.ok(emailService.sendMail(emailMessage));
    }
}