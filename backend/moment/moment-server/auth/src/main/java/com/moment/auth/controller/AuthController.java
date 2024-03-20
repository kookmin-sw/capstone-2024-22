package com.moment.auth.controller;

import com.moment.auth.common.APIResponse;
import com.moment.auth.common.code.SuccessCode;
import com.moment.auth.dto.request.AuthRequest;
import com.moment.auth.dto.response.TokenResponseDTO;
import com.moment.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<APIResponse<TokenResponseDTO.GetToken>> login(
            @RequestBody AuthRequest.Login login
            ) {
        TokenResponseDTO.GetToken responseDTO = authService.login(login);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, responseDTO));
    }

    // 이메일 인증코드 전송
    @PostMapping("/code")
    public ResponseEntity<APIResponse<TokenResponseDTO.GetTempToken>> sendCode(
            @RequestBody AuthRequest.SendCode sendCode
            ) {
        TokenResponseDTO.GetTempToken tempToken = authService.sendCode(sendCode);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, tempToken));
    }

    // 인증코드 검증
    @PatchMapping("/verify")
    public ResponseEntity<APIResponse> verifyCode(
            @RequestBody AuthRequest.VerifyCode verifyCode,
            @RequestHeader String userId
            ) {
        authService.verifyCode(verifyCode, Long.parseLong(userId));
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }

    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity<APIResponse> changePassword(
            @RequestBody AuthRequest.ChangePassword changePassword
            ) {
        authService.changePassword(changePassword);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }
}
