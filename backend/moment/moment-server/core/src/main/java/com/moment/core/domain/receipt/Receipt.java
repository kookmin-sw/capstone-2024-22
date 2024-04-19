package com.moment.core.domain.receipt;

import com.moment.core.domain.BaseEntity;
import com.moment.core.domain.trip.Trip;
import com.moment.core.domain.tripFile.TripFile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "receipt")
public class Receipt extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = true)
    private Trip trip;

    // 최대 길이 7
    @Column(name = "main_departure", nullable = false, length = 7)
    private String mainDeparture;

    @Column(name = "sub_departure", nullable = false, length = 24)
    private String subDeparture;

    @Column(name = "main_destination", nullable = false, length = 7)
    private String mainDestination;

    @Column(name = "sub_destination", nullable = false, length = 24)
    private String subDestination;

    @Column(name = "one_line_memo", nullable = false, length = 27)
    private String oneLineMemo;

    @Column(name = "num_of_card", nullable = false)
    private Integer numOfCard;

    @Column(name = "st_date", nullable = false)
    private LocalDate stDate;

    @Column(name = "ed_date", nullable = false)
    private LocalDate edDate;

    @Column(name = "trip_name", nullable = false)
    private String tripName;

    // happy
    @Column(name = "happy")
    private Float happy;

    // sad
    @Column(name = "sad")
    private Float sad;

    // angry
    @Column(name = "angry")
    private Float angry;

    // neutral
    @Column(name = "neutral")
    private Float neutral;

    @Column(name = "receipt_theme_type", nullable = false)
    private String receiptThemeType;
}
