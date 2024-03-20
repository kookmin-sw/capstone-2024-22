package com.moment.auth.domain.user;

import com.moment.auth.domain.BaseEntity;
import com.moment.auth.domain.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User extends BaseEntity {
    // 유저 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 유저 로그인 ID
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // 유저 비밀번호
    @Column(name = "password", nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "verification_code")
    private String verificationCode;

}
