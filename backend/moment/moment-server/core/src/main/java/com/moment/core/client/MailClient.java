package com.moment.core.client;

import com.moment.core.dto.request.EmailMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "mail-service")
public interface MailClient {

    @PostMapping("/email")
    ResponseEntity<String> sendMail(EmailMessageDTO emailMessageDTO);
}
