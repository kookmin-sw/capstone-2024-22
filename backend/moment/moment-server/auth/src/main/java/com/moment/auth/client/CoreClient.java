package com.moment.auth.client;

import com.moment.auth.common.APIResponse;
import com.moment.auth.dto.request.EmailMessageDTO;
import com.moment.auth.dto.request.UserRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "core-service")
public interface CoreClient {

    @PostMapping("/core/user/register")
    ResponseEntity<APIResponse> registerUser(UserRequestDTO.registerUser request);

    @DeleteMapping("/core/user/{userId}")
    ResponseEntity<APIResponse> deleteUser(@PathVariable Long userId);

    @GetMapping("/core/user/test")
    ResponseEntity<String> test();
}
