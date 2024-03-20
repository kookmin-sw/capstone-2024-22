package com.moment.core.dto.request;

import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    @Setter
    public static class registerUser {

        private String email;

        private Long id;


    }
}
