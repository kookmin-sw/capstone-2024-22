package com.moment.core.controller;

import com.moment.core.service.S3Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@Slf4j
public class RootController {

    private final Environment env;
    private final S3Service s3Service;

    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        // 서버 아이디와 포트 출력
        log.info("health-check called" );
        return ResponseEntity.ok("I'm alive : " + env.getProperty("server.port"));
    }

    @PostMapping("/test/s3")
    public ResponseEntity<String> testS3(
            @RequestHeader Long userId,
            @RequestPart MultipartFile recordFile
    ) {
        try {
            return ResponseEntity.ok(s3Service.uploadFile(recordFile, userId, LocalDateTime.now().toString(), true));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/test/s3/folder")
    public ResponseEntity<String> testS3Folder(
            @RequestHeader Long userId,
            @RequestParam String folderName
    ) {
        s3Service.createFolder(userId.toString());
        return ResponseEntity.ok("test-s3-folder called");
    }

    @DeleteMapping("/test/s3")
    public ResponseEntity<String> deleteS3(
            @RequestHeader Long userId,
            @RequestParam String fileName
    ) {
//        fileName = "users/" + userId.toString() + "/" + fileName;
        fileName = "users/" + userId.toString();
        s3Service.deleteFile(fileName, userId.toString());
        return ResponseEntity.ok("delete-s3 called");
    }
}
