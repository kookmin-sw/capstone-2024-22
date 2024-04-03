package com.moment.scheduler.domain.user;


import com.moment.scheduler.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    @Column(name = "id")
    private Long id;

    // 유저 로그인 ID
    @Column(name = "email", nullable = false, unique = true)
    private String email;


}
