package com.moment.core.domain.alreadyBookedDate;

import com.moment.core.domain.BaseEntity;
import com.moment.core.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "already_booked_date")
public class AlreadyBookedDate extends BaseEntity {

    // 여행 생성 불가능 일자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 날짜
    @Column(name = "year_date", nullable = false)
    private LocalDate yearDate;

}
