package com.moment.core.controller;

import com.moment.core.domain.trip.Trip;
import com.moment.core.dto.response.TripFileResponseDTO;
import com.moment.core.service.TripFileService;
import com.moment.core.service.TripService;
import com.moment.core.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.moment.core.config.DocumentFormatGenerator.getDateFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class TripFileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripFileService tripFileService;
    @MockBean
    private TripService tripService;
    @MockBean
    private UserService userService;

    @InjectMocks
    private TripFileController tripFileController;

    @Test
    void getTripFiles() throws Exception {
        List<TripFileResponseDTO.GetTripFile> tripFiles = List.of(
                TripFileResponseDTO.GetTripFile.builder()
                        .id(1L)
                        .tripId(1L)
                        .email("test")
                        .yearDate(LocalDate.now())
                        .analyzingCount(1)
                        .build(),
                TripFileResponseDTO.GetTripFile.builder()
                        .id(2L)
                        .tripId(2L)
                        .email("test")
                        .yearDate(LocalDate.now().plusDays(1))
                        .analyzingCount(2)
                        .build(),
                TripFileResponseDTO.GetTripFile.builder()
                        .id(3L)
                        .tripId(3L)
                        .email("test")
                        .yearDate(LocalDate.now().plusDays(2))
                        .analyzingCount(1)
                        .build()
        );
        TripFileResponseDTO.GetAllTripFile allTripFile = TripFileResponseDTO.GetAllTripFile.builder()
                .tripFiles(tripFiles)
                .build();

        Mockito.when(tripFileService.getTripFiles(any(Long.class))).thenReturn(allTripFile);
        Mockito.doNothing().when(userService).validateUserWithTrip(any(Long.class), any(Long.class));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/core/tripfile/{tripId}", 1L)
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("tripfile/getTripFiles",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("userId").description("Bearer Token")
                        ),
                        pathParameters(
                                parameterWithName("tripId").description("여행 ID")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                fieldWithPath("data.tripFiles[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("data.tripFiles[].tripId").type(JsonFieldType.NUMBER).description("여행 ID"),
                                fieldWithPath("data.tripFiles[].email").type(JsonFieldType.STRING).description("유저 email"),
                                fieldWithPath("data.tripFiles[].yearDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("연월일"),
                                fieldWithPath("data.tripFiles[].analyzingCount").type(JsonFieldType.NUMBER).description("분석 중 파일 개수")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void getUntitledTripFiles() throws Exception {
        List<TripFileResponseDTO.GetTripFile> tripFiles = List.of(
                TripFileResponseDTO.GetTripFile.builder()
                        .id(1L)
                        .tripId(1L)
                        .email("test")
                        .yearDate(LocalDate.now())
                        .analyzingCount(1)
                        .build(),
                TripFileResponseDTO.GetTripFile.builder()
                        .id(2L)
                        .tripId(2L)
                        .email("test")
                        .yearDate(LocalDate.now().plusDays(1))
                        .analyzingCount(2)
                        .build(),
                TripFileResponseDTO.GetTripFile.builder()
                        .id(3L)
                        .tripId(3L)
                        .email("test")
                        .yearDate(LocalDate.now().plusDays(2))
                        .analyzingCount(1)
                        .build()
        );
        TripFileResponseDTO.GetAllTripFile allTripFile = TripFileResponseDTO.GetAllTripFile.builder()
                .tripFiles(tripFiles)
                .build();
        Mockito.when(tripService.getUntitledTripById(any(Long.class))).thenReturn(new Trip());
        Mockito.when(tripFileService.getTripFiles(any())).thenReturn(allTripFile);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/core/tripfile/untitled")
                        .header("userId", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("tripfile/getUntitledTripFiles",
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
                                fieldWithPath("data.tripFiles[].id").type(JsonFieldType.NUMBER).description("파일 ID"),
                                fieldWithPath("data.tripFiles[].tripId").type(JsonFieldType.NUMBER).description("여행 ID"),
                                fieldWithPath("data.tripFiles[].email").type(JsonFieldType.STRING).description("유저 email"),
                                fieldWithPath("data.tripFiles[].yearDate").type(JsonFieldType.STRING).attributes(getDateFormat()).description("연월일"),
                                fieldWithPath("data.tripFiles[].analyzingCount").type(JsonFieldType.NUMBER).description("분석 중 파일 개수")
                                )
                        )
                )
                .andDo(print());
    }
}