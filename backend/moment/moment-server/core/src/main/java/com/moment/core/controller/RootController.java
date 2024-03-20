package com.moment.core.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class RootController {

    private final Environment env;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        // 서버 아이디와 포트 출력
        log.info("health-check called" );
        return ResponseEntity.ok("I'm alive : " + env.getProperty("server.port"));
    }
}
