package com.moment.auth.controller;

import com.moment.auth.client.CoreClient;
import com.moment.auth.common.APIResponse;
import com.moment.auth.common.code.SuccessCode;
import com.moment.auth.dto.request.AuthRequest;
import com.moment.auth.dto.response.TokenResponseDTO;
import com.moment.auth.service.AuthService;
import com.moment.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final CoreClient coreClient;
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<APIResponse<TokenResponseDTO.GetToken>> login(
            @RequestBody AuthRequest.Login login
            ) {
        TokenResponseDTO.GetToken responseDTO = authService.login(login);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, responseDTO));
    }

    // 이메일 인증코드 전송
    // 회원가입이라면 해당 이메일이 미가입되어있어야하고
    // 비밀번호 찾기라면 해당 이메일이 가입되어있어야함
    @PostMapping("/code")
    @Operation(summary = "이메일 인증코드 전송 요청", description = "회원가입 또는 비밀번호찾기에서 이메일인증 요청을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식",content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "402", description = "기회원 또는 미회원",content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<APIResponse<TokenResponseDTO.GetTempToken>> sendCode(
            @RequestBody AuthRequest.SendCode sendCode
            ) {
        TokenResponseDTO.GetTempToken tempToken = authService.sendCode(sendCode);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, tempToken));
    }

    // 인증코드 검증
    @PatchMapping("/verify")
    public ResponseEntity<APIResponse<TokenResponseDTO.GetToken>> verifyCode(
            @RequestBody AuthRequest.VerifyCode verifyCode,
            @RequestHeader Long userId
            ) {
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS, authService.verifyCode(verifyCode, userId)));
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<APIResponse> changePassword(
            @RequestBody AuthRequest.ChangePassword changePassword,
            @RequestHeader Long userId
            ) {
        authService.changePassword(changePassword, userId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse> deleteAccount(
            @RequestHeader Long userId
            ) {
        userService.deleteAccount(userId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS));
    }

    // 테스트
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("test");
        coreClient.test();
        return new ResponseEntity<>("test", HttpStatus.OK);
    }
}
