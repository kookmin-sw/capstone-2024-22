package com.moment.auth.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;


public class AuthRequest {

    //자체회원가입 폼
    @Getter
    @Setter
    @Builder
    public static class Login {

        private String email;

        private String password;


    }

    @Getter
    @Setter
    @Builder
    public static class SendCode {
        private String email;
        private String isSignUp;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerifyCode {
        private String code;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePassword {

        private String code;
        private String newPassword;
    }
}
