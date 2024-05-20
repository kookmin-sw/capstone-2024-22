package com.moment.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moment.core.dto.Pagination;
import com.moment.core.dto.request.ReceiptRequestDTO;
import com.moment.core.service.ReceiptService;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.moment.core.config.DocumentFormatGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReceiptService receiptService;
    @InjectMocks
    private ReceiptController receiptController;

    @Test
    void getReceiptCount() throws Exception {
        long count = 10L;
        ReceiptRequestDTO.getReceiptCount getReceiptCount = ReceiptRequestDTO.getReceiptCount.builder()
                .count(count)
                .build();

        Mockito.when(receiptService.getReceiptCount(1L)).thenReturn((int) count);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/core/receipt/count")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("receipt/getReceiptCount",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data.count").type(JsonFieldType.NUMBER).description("영수증 개수")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void getAllReceipt() throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = Pageable.ofSize(size).withPage(page);

        ReceiptRequestDTO.getReceiptAll getReceiptAll = ReceiptRequestDTO.getReceiptAll.builder()
                .receiptList(List.of(
                        ReceiptRequestDTO.getReceipt.builder()
                                .id(1L)
                                .tripId(1L)
                                .tripName("tripName1")
                                .mainDeparture("mainDeparture")
                                .subDeparture("subDeparture")
                                .mainDestination("mainDestination")
                                .subDestination("subDestination")
                                .oneLineMemo("oneLineMemo")
                                .receiptThemeType("A")
                                .happy(40.0f)
                                .sad(20.0f)
                                .angry(20.95f)
                                .neutral(19.05f)
                                .disgust(0.0f)
                                .stDate(LocalDate.parse("2021-01-01"))
                                .edDate(LocalDate.parse("2021-01-03"))
                                .numOfCard(3)
                                .createdAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                                .build(),
                        ReceiptRequestDTO.getReceipt.builder()
                                .id(2L)
                                .tripId(1L)
                                .tripName("tripName1")
                                .mainDeparture("mainDeparture")
                                .subDeparture("subDeparture")
                                .mainDestination("mainDestination")
                                .subDestination("subDestination")
                                .oneLineMemo("oneLineMemo")
                                .receiptThemeType("B")
                                .happy(40.0f)
                                .sad(20.0f)
                                .angry(20.95f)
                                .neutral(19.05f)
                                .disgust(0.0f)
                                .stDate(LocalDate.parse("2021-02-01"))
                                .edDate(LocalDate.parse("2021-02-02"))
                                .numOfCard(3)
                                .createdAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                                .build(),
                        ReceiptRequestDTO.getReceipt.builder()
                                .id(3L)
                                .tripId(1L)
                                .tripName("tripName1")
                                .mainDeparture("mainDeparture")
                                .subDeparture("subDeparture")
                                .mainDestination("mainDestination")
                                .subDestination("subDestination")
                                .oneLineMemo("oneLineMemo")
                                .receiptThemeType("A")
                                .happy(40.0f)
                                .sad(20.0f)
                                .angry(20.95f)
                                .neutral(19.05f)
                                .disgust(0.0f)
                                .stDate(LocalDate.parse("2021-03-01"))
                                .edDate(LocalDate.parse("2021-03-02"))
                                .numOfCard(3)
                                .createdAt(LocalDateTime.parse("2021-01-01T00:00:00"))
                                .build()
                ))
                .pagination(Pagination.builder()
                        .totalPages(1)
                        .totalElements(10L)
                        .currentPage(0)
                        .currentElements(3)
                        .build())
                .build();

        Mockito.when(receiptService.getAllReceipt(1L, pageable)).thenReturn(getReceiptAll);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/core/receipt/all")
                .header("userId", 1L)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("receipt/getAllReceipt",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                queryParameters(
                                    parameterWithName("page").description("페이지 번호"),
                                    parameterWithName("size").description("페이지 크기")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data.receiptList").type(JsonFieldType.ARRAY).description("영수증 리스트"),
                                        fieldWithPath("data.receiptList[].id").type(JsonFieldType.NUMBER).description("영수증 ID"),
                                        fieldWithPath("data.receiptList[].tripId").type(JsonFieldType.NUMBER).description("여행 ID"),
                                        fieldWithPath("data.receiptList[].mainDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("출발지(대)"),
                                        fieldWithPath("data.receiptList[].subDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("출발지(소)"),
                                        fieldWithPath("data.receiptList[].mainDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("도착지(대)"),
                                        fieldWithPath("data.receiptList[].subDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("도착지(소)"),
                                        fieldWithPath("data.receiptList[].oneLineMemo").type(JsonFieldType.STRING).attributes(getStringNumFormat(27)).description("한줄 메모"),
                                        fieldWithPath("data.receiptList[].numOfCard").type(JsonFieldType.NUMBER).description("카드뷰 개수"),
                                        fieldWithPath("data.receiptList[].stDate").type(JsonFieldType.STRING).description("시작 날짜"),
                                        fieldWithPath("data.receiptList[].edDate").type(JsonFieldType.STRING).description("종료 날짜"),
                                        fieldWithPath("data.receiptList[].tripName").type(JsonFieldType.STRING).description("여행 이름"),
                                        fieldWithPath("data.receiptList[].happy").type(JsonFieldType.NUMBER).description("행복한 감정 비율"),
                                        fieldWithPath("data.receiptList[].sad").type(JsonFieldType.NUMBER).description("슬픈 감정 비율"),
                                        fieldWithPath("data.receiptList[].angry").type(JsonFieldType.NUMBER).description("화난 감정 비율"),
                                        fieldWithPath("data.receiptList[].neutral").type(JsonFieldType.NUMBER).description("중립적인 감정 비율"),
                                        fieldWithPath("data.receiptList[].disgust").type(JsonFieldType.NUMBER).description("역겨운 감정 비율"),
                                        fieldWithPath("data.receiptList[].receiptThemeType").type(JsonFieldType.STRING).attributes(getReceiptFormat()).description("영수증 테마 타입"),
                                        fieldWithPath("data.receiptList[].createdAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("생성 날짜"),
                                        fieldWithPath("data.pagination").type(JsonFieldType.OBJECT).description("페이징 정보"),
                                        fieldWithPath("data.pagination.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수"),
                                        fieldWithPath("data.pagination.totalElements").type(JsonFieldType.NUMBER).description("전체 요소 수"),
                                        fieldWithPath("data.pagination.currentPage").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("data.pagination.currentElements").type(JsonFieldType.NUMBER).description("현재 요소 수")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void deleteReceipts() throws Exception {
        ReceiptRequestDTO.deleteReceipt deleteReceipt1 = ReceiptRequestDTO.deleteReceipt.builder()
                .receiptId(1L)
                .build();
        ReceiptRequestDTO.deleteReceipt deleteReceipt2 = ReceiptRequestDTO.deleteReceipt.builder()
                .receiptId(2L)
                .build();
        ReceiptRequestDTO.deleteReceipt deleteReceipt3 = ReceiptRequestDTO.deleteReceipt.builder()
                .receiptId(3L)
                .build();
        ReceiptRequestDTO.deleteReceipts deleteReceipts = ReceiptRequestDTO.deleteReceipts.builder()
                .receiptIds(List.of(deleteReceipt1, deleteReceipt2, deleteReceipt3))
                .build();

        Mockito.doNothing().when(receiptService).deleteReceipts(1L, deleteReceipts);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/core/receipt/delete")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(deleteReceipts))
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("receipt/deleteReceipts",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestFields(
                                        fieldWithPath("receiptIds").type(JsonFieldType.ARRAY).description("삭제할 영수증 ID 리스트"),
                                        fieldWithPath("receiptIds[].receiptId").type(JsonFieldType.NUMBER).description("영수증 ID")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 없음")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void createReceipt() throws Exception {
        ReceiptRequestDTO.createReceipt createReceipt = ReceiptRequestDTO.createReceipt.builder()
                .tripId(1L)
                .mainDeparture("mainDeparture")
                .subDeparture("subDeparture")
                .mainDestination("mainDestination")
                .subDestination("subDestination")
                .oneLineMemo("oneLineMemo")
                .receiptThemeType("A")
                .build();

        Mockito.doNothing().when(receiptService).createReceipt(1L, createReceipt);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/core/receipt")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(createReceipt))
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("receipt/createReceipt",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestFields(
                                        fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 ID"),
                                        fieldWithPath("mainDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("출발지(대)"),
                                        fieldWithPath("subDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("출발지(소)"),
                                        fieldWithPath("mainDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("도착지(대)"),
                                        fieldWithPath("subDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("도착지(소)"),
                                        fieldWithPath("oneLineMemo").type(JsonFieldType.STRING).attributes(getStringNumFormat(27)).description("한줄 메모"),
                                        fieldWithPath("receiptThemeType").type(JsonFieldType.STRING).attributes(getReceiptFormat()).description("영수증 테마 타입")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 없음")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    public void updateReceipt() throws Exception {
        ReceiptRequestDTO.updateReceipt updateReceipt = ReceiptRequestDTO.updateReceipt.builder()
                .id(1L)
                .mainDeparture("mainDeparture")
                .subDeparture("subDeparture")
                .mainDestination("mainDestination")
                .subDestination("subDestination")
                .oneLineMemo("oneLineMemo")
                .receiptThemeType("A")
                .build();

        Mockito.doNothing().when(receiptService).updateReceipt(1L, updateReceipt);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/core/receipt")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(updateReceipt))
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("receipt/updateReceipt",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("영수증 ID"),
                                        fieldWithPath("mainDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("출발지(대)"),
                                        fieldWithPath("subDeparture").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("출발지(소)"),
                                        fieldWithPath("mainDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(7)).description("도착지(대)"),
                                        fieldWithPath("subDestination").type(JsonFieldType.STRING).attributes(getStringNumFormat(24)).description("도착지(소)"),
                                        fieldWithPath("oneLineMemo").type(JsonFieldType.STRING).attributes(getStringNumFormat(27)).description("한줄 메모"),
                                        fieldWithPath("receiptThemeType").type(JsonFieldType.STRING).attributes(getReceiptFormat()).description("영수증 테마 타입")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 없음")
                                )
                        )
                )
                .andDo(print());

}   }