package com.moment.scheduler.service;

import com.moment.scheduler.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotiService {
    private final UserRepository userRepository;

    public void sendNoti() {
        log.info("send noti");
    }
}
