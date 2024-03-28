package com.moment.auth.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;



public class AuthRequest {

    //자체회원가입 폼
    @Getter
    @Setter
    public static class Login {

        private String email;

        private String password;


    }

    @Getter
    @Setter
    public static class SendCode {
        private String email;
        private boolean isSignUp;
    }

    @Getter
    @Setter
    public static class VerifyCode {
        private String code;
    }

    @Getter
    @Setter
    public static class ChangePassword {

        private String code;
        private String newPassword;
    }
}
