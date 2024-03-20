package com.moment.auth.client;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.moment.auth.common.ErrorResponse;
import com.moment.auth.exception.FeignClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FeignClientExceptionErrorDecoder implements ErrorDecoder {
    private ObjectMapper objectMapper = new ObjectMapper();
    private StringDecoder stringDecoder = new StringDecoder();


    @Override
    public FeignClientException decode(String methodKey, Response response) {
        String message = null;
        ErrorResponse errorForm = null;
        if (response.body() != null) {
            try {
                message = stringDecoder.decode(response, String.class).toString();
                errorForm = objectMapper.readValue(message, ErrorResponse.class);
            } catch (IOException e) {
                log.error(methodKey + "Error Deserializing response body from failed feign request response.", e);
            }
        }
        return new FeignClientException(response.status(), message, response.headers(), errorForm);
    }
}