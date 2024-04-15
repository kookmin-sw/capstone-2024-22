package com.moment.scheduler.service;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Service
public class AwsService {
    String path = "https://vttrz7x4quryall56hmpfabps40mwauu.lambda-url.ap-northeast-2.on.aws/";

    public void turnOnOrOff(String instanceName, boolean isStart) {
        // path+"start-ec2" Post 로 요청
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("instance_name", instanceName);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // request body 생성


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        String url = isStart ? path + "start-ec2" : path + "stop-ec2";

        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("AWS 서버에 요청에 실패했습니다.");
        }
    }

    public Boolean isEc2Running() {
        // path+"is-ec2-running" Post 로 요청
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();
        String url = path;

        ResponseEntity<String> response = rt.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );
        if(response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("AWS 서버에 요청에 실패했습니다.");
        }
        return response.getBody().equals("true");
    }
}
