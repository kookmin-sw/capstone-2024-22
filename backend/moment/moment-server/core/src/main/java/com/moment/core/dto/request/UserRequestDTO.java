package com.moment.core.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class registerUser {

        private String email;

        private Long id;

        private boolean notification;

        private boolean dataUsage;

        private String firebaseToken;
    }

    @Getter
    @Setter
    @Builder
    public static class updateUser {

        private String email;

        private boolean notification;

        private boolean dataUsage;

        private String firebaseToken;
    }
}
