package com.moment.auth.service;

import com.moment.auth.client.MailClient;
import com.moment.auth.dto.request.EmailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final MailClient mailClient;


    public String sendMail(String to, String subject, String message) {
        return mailClient.sendMail(EmailMessage.builder()
                        .message(message)
                        .subject(subject)
                        .to(to)
                .build()
        ).getBody();
    }
}
