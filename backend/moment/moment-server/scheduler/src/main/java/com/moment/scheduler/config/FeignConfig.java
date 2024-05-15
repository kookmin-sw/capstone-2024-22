package com.moment.scheduler.config;

import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class FeignConfig {

    @Bean
    public Request.Options requestOptions() {
        long connectionTimeout = 10000000;
        long readTimeout = 10000000; // read - timeout 5초로 설정
        return new Request.Options(connectionTimeout, TimeUnit.SECONDS, readTimeout, TimeUnit.SECONDS, false);
    }

    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }
}