package com.moment.auth.dto.request;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {
    private String to;

    private String subject;

    private String message;
}
