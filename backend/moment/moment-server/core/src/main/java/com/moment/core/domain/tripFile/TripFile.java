package com.moment.core.domain.tripFile;

import com.moment.core.domain.BaseEntity;
import com.moment.core.domain.trip.Trip;
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
@Table(name = "trip_file",
    indexes = {
        // User + YearDate로 해당 유저의 해당 연월의 파일이 유니크해야함, 조회 가능해야함
        @Index(name = "trip_file_index1", columnList = "user_id, year_date", unique = true),
    }
)
public class TripFile extends BaseEntity {
    // 파일 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 여행 아이디
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    // 유저
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 연월일
    @Column(name = "year_date", nullable = false)
    private LocalDate yearDate;

    // 분석 중 파일 개수
    @Column(name = "analyzing_count", nullable = false)
    private Integer analyzingCount;

}
