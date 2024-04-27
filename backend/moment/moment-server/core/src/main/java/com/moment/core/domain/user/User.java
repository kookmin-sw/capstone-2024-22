package com.moment.core.domain.user;

import com.moment.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseEntity{
    // 유저 아이디
    @Id
    @Column(name = "id")
    private Long id;

    // 유저 로그인 ID
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 알림설정 유무
    @Column(name = "notification", nullable = false)
    private boolean notification;

    // 데이터 사용 유무
    @Column(name = "data_usage", nullable = false)
    private boolean dataUsage;

    // firebase token
    @Column(name = "firebase_token")
    private String firebaseToken;


}
