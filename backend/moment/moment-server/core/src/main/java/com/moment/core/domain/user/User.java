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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 유저 로그인 ID
    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    // 유저 비밀번호
    @Column(name = "password", nullable = false)
    private String password;
}
