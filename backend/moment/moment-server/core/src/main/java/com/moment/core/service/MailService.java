package com.moment.core.service;

import com.moment.core.client.MailClient;
import com.moment.core.dto.request.EmailMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final MailClient mailClient;

    @Async
    public void sendMail(String to, String subject, String message) {
        mailClient.sendMail(EmailMessageDTO.builder()
                        .message(message)
                        .subject(subject)
                        .to(to)
                .build()
        );
    }
}
