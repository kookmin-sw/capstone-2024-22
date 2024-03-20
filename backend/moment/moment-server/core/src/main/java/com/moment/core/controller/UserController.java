package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.domain.user.User;
import com.moment.core.dto.request.UserRequestDTO;
import com.moment.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/core/user")
public class UserController {
    private final UserService userService;

    // 유저 등록
    @PostMapping("/register")
    @Operation(summary = "유저 등록", description = "유저를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 유저아이디일 경우",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> registerUser(
            @RequestBody UserRequestDTO.registerUser request
            ) {
        userService.save(request);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS));
    }
}
