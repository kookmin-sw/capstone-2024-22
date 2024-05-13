package com.moment.scheduler.domain.cardView;


import com.moment.scheduler.domain.BaseEntity;
import com.moment.scheduler.domain.tripFile.TripFile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card_view")
public class CardView extends BaseEntity {
    // 레코드 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // tripfile_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_file_id", nullable = false)
    private TripFile tripFile;

//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;

    // 녹음한 시점
    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    // 파일이름
    @Column(name = "record_file_name", nullable = false)
    private String recordFileName;

    // 파일 저장 위치
    @Column(name = "record_file_url", nullable = false)
    private String recordFileUrl;

    // 위치
    @Column(name = "location", nullable = false)
    private String location;

    // 파일길이
    @Column(name = "record_file_length", nullable = false)
    private Long recordFileLength;

    // 날씨
    @Column(name = "weather", nullable = false)
    private String weather;

    // 온도
    @Column(name = "temperature", nullable = false)
    private String temperature;

    // STT
    @Column(name = "stt", columnDefinition = "TEXT")
    private String stt;

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

    // disgust
    @Column(name = "disgust")
    private Float disgust;

    // 질문
    @Column(name = "question", nullable = false)
    private String question;

    // 즐겨찾기 유무
    @Column(name = "is_loved", nullable = false)
    private Boolean isLoved;

    // status
    // 1. 처리 대기 중 -> WAIT
    // 2. 처리 완료 -> DONE
    // 3. 처리 실패 -> FAIL
    @Column(name = "record_file_status", nullable = false)
    private String recordFileStatus;

}
