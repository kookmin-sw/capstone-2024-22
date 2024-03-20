package com.moment.auth.dto.response;

import com.moment.auth.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


public class TokenResponseDTO {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class GetToken {
        private String grantType;
        private String accessToken;
        private String refreshToken;
        private Role role;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    public static class GetTempToken {
        private String grantType;
        private String accessToken;
        private Role role;
        private Long userId;
    }


}
