package com.moment.auth.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    @Builder
    public static class registerUser {

        private String email;

        private Long id;


    }
}
