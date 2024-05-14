package com.moment.core.dto.request;

import com.moment.core.dto.Pagination;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ReceiptRequestDTO {

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class getReceiptCount {
        private Long count;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class getReceipt {
        private Long id;

        private Long tripId;

        private String mainDeparture;

        private String subDeparture;

        private String mainDestination;

        private String subDestination;

        private String oneLineMemo;

        private Integer numOfCard;

        private LocalDate stDate;

        private LocalDate edDate;

        private String tripName;

        private Float happy;

        private Float sad;

        private Float angry;

        private Float neutral;

        private Float disgust;

        private String receiptThemeType;

        private LocalDateTime createdAt;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class getReceiptAll {
        private List<getReceipt> receiptList;
        private Pagination pagination;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class deleteReceipts {
        private List<deleteReceipt> receiptIds;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class deleteReceipt {
        private Long receiptId;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class createReceipt {

        private Long tripId;

        private String mainDeparture;

        private String subDeparture;

        private String mainDestination;

        private String subDestination;

        private String oneLineMemo;

        private String receiptThemeType;
    }

    @Data
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class updateReceipt {

        private Long id;

        private String mainDeparture;

        private String subDeparture;

        private String mainDestination;

        private String subDestination;

        private String oneLineMemo;

        private String receiptThemeType;
    }
}