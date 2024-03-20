package com.moment.core.domain.imageFile;

import com.moment.core.domain.cardView.CardView;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "image_file")
public class ImageFile {
    // 이미지 파일 아이디
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // cardview 아이디
    @ManyToOne
    @JoinColumn(name = "card_view_id", nullable = false)
    private CardView cardView;

    // 파일 경로
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    // 파일 이름
    @Column(name = "file_name", nullable = false)
    private String fileName;


}
