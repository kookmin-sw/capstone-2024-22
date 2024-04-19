package com.moment.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    // 총 페이지 수
    private Integer totalPages;
    // 총 갯수
    private Long totalElements;
    // 현재 페이지
    private Integer currentPage;
    // 현재 페이지에 갖고 있는 element 수
    private Integer currentElements;
}
