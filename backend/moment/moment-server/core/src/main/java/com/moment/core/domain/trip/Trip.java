package com.moment.core.domain.trip;

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
@Table(name = "trip")
public class Trip extends BaseEntity {

    // 여행 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 유저
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 출발일
    @Column(name = "start_date")
    private LocalDate startDate;

    // 도착일
    @Column(name = "end_date")
    private LocalDate endDate;

    // 분석 중 파일 개수
    @Column(name = "analyzing_count", nullable = false)
    private Integer analyzingCount;

    // 여행 이름
    @Column(name = "trip_name", nullable = false)
    private String tripName;

    // 묶이지 않은 여행
    @Column(name = "is_not_titled", nullable = false)
    private Boolean isNotTitled;
}
