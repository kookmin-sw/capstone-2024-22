package com.moment.gateway.dto.request;


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
    }

    @Getter
    @Setter
    public static class VerifyCode {
        private Long userId;
        private String code;
    }

    @Getter
    @Setter
    public static class ChangePassword {
        private Long userId;
        private String code;
        private String newPassword;
    }
}
