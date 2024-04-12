package com.moment.core.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.moment.core.dto.response.AlreadyBookedDateResponseDTO;
import com.moment.core.service.AlreadyBookedDateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AlreadyBookedDateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlreadyBookedDateService alreadyBookedDateService;

    @InjectMocks
    private AlreadyBookedDateController alreadyBookedDateController;

    @Test
    void testGetAll() throws Exception {
        // Mock 데이터 생성
        Long userId = 1L;
        // 목업 객체 return
        AlreadyBookedDateResponseDTO.GetCardView getCardView1 = AlreadyBookedDateResponseDTO.GetCardView.builder()
                .bookedDate(LocalDate.now())
                .build();
        AlreadyBookedDateResponseDTO.GetCardView getCardView2 = AlreadyBookedDateResponseDTO.GetCardView.builder()
                .bookedDate(LocalDate.of(2023, 10, 10))
                .build();
        AlreadyBookedDateResponseDTO.GetAllCardView mockResponseDTO = AlreadyBookedDateResponseDTO.GetAllCardView.builder()
                .cardViews(List.of(getCardView1, getCardView2))
                .build();


        // Mock Service의 동작 설정
        when(alreadyBookedDateService.getAll(userId)).thenReturn(mockResponseDTO);

        // MockMvc를 사용하여 컨트롤러 호출 및 결과 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/core/alreadyBookedDate/all")
                        .header("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // 추가적인 검증 필요시, JSON 응답을 파싱하여 내용을 검증할 수 있음
                //.andExpect(jsonPath("$.data").value(mockResponseDTO))
                .andDo(
                        document(
                                "alreadyBookedDate/getAll",
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
                                        fieldWithPath("data.cardViews[].bookedDate").type(JsonFieldType.STRING).description("예약된 날짜 목록")
                                )
                        )
                )
                .andDo(print());

        ;

        // 서비스가 정확히 호출되었는지 확인
        //        verify(alreadyBookedDateService, times(1)).getAll(userId);
    }
}
