package com.moment.core.dto.request;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDTO {
    private String to;

    private String subject;

    private String message;
}
