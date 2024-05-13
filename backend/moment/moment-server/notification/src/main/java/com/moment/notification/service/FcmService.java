package com.moment.notification.service;

import com.moment.notification.DTO.FcmMessageDto;
import com.moment.notification.DTO.FcmSendDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.auth.oauth2.GoogleCredentials;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;



import java.io.IOException;

import java.util.List;

@Service
@Slf4j
public class FcmService {
    public int sendMessageTo(FcmSendDto fcmSendDto) throws IOException {

        String message = makeMessage(fcmSendDto);
//        RestTemplate restTemplate = new RestTemplate();
//
//
//        final HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken());
//
//        final HttpEntity<String> httpEntity = new HttpEntity<>(message, httpHeaders);
//
//
        String API_URL = "https://fcm.googleapis.com/v1/projects/moment-82b0f/messages:send";
//        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, httpEntity, String.class);
//
//        System.out.println(response.getStatusCode());
//
//        return response.getStatusCode() == HttpStatus.OK ? 1 : 0;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        log.info("response : {}", response.body().string());
        return response.code();
    }

    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {

        ObjectMapper om = new ObjectMapper();
        FcmMessageDto fcmMessageDto = FcmMessageDto.builder()
                .message(FcmMessageDto.Message.builder()
                        .token(fcmSendDto.getToken())
                        .notification(FcmMessageDto.Notification.builder()
                                .title(fcmSendDto.getTitle())
                                .body(fcmSendDto.getBody())
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return om.writeValueAsString(fcmMessageDto);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/fcmkey.json";

        final GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }

}
