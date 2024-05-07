package com.moment.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moment.core.domain.trip.Trip;
import com.moment.core.dto.request.CardViewRequestDTO;
import com.moment.core.dto.request.TripRequestDTO;
import com.moment.core.dto.response.TripResponseDTO;
import com.moment.core.service.TripService;
import com.moment.core.service.UserService;
import io.swagger.v3.core.util.Json;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static com.moment.core.config.DocumentFormatGenerator.getDateFormat;
import static com.moment.core.config.DocumentFormatGenerator.getDateTimeFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static reactor.core.publisher.Mono.when;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;
    @MockBean
    private UserService userService;

    @InjectMocks
    private TripController tripController;


    @Test
    void registerTrip() throws Exception {
        TripRequestDTO.RegisterTrip registerTrip = TripRequestDTO.RegisterTrip.builder()
                .tripName("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .build();
        Mockito.doNothing().when(tripService).register(any(TripRequestDTO.RegisterTrip.class), any(Long.class));

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/core/trip/register")
                .header("userId", 1L)
                .content(Json.pretty(registerTrip))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andDo(
                        document("trip/register",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestFields(
                                        fieldWithPath("tripName").description("여행 이름"),
                                        fieldWithPath("startDate").attributes(getDateFormat()).description("여행 시작 날짜"),
                                        fieldWithPath("endDate").attributes(getDateFormat()).description("여행 종료 날짜")
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
                .andDo(print())
        ;
    }

    @Test
    void getAllTrip() throws Exception{
        List<TripResponseDTO.GetTrip> tripList = List.of(
                TripResponseDTO.GetTrip.builder()
                        .id(1L)
                        .email("test")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .analyzingCount(0)
                        .tripName("test")
                        .build(),
                TripResponseDTO.GetTrip.builder()
                        .id(2L)
                        .email("test")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .analyzingCount(1)
                        .tripName("test")
                        .build(),
                TripResponseDTO.GetTrip.builder()
                        .id(3L)
                        .email("test")
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .analyzingCount(2)
                        .tripName("test")
                        .build()
        );
        TripResponseDTO.GetAllTrip allTrip = TripResponseDTO.GetAllTrip.builder()
                .trips(tripList)
                .build();
        Mockito.when(tripService.getAllTrip(any(Long.class))).thenReturn(allTrip);


        mockMvc.perform(MockMvcRequestBuilders.get("/core/trip/all")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("trip/getAll",
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
                                        fieldWithPath("data.trips[].id").type(JsonFieldType.NUMBER).description("여행 ID"),
                                        fieldWithPath("data.trips[].email").type(JsonFieldType.STRING).description("유저 email"),
                                        fieldWithPath("data.trips[].startDate").attributes(getDateFormat()).description("출발일"),
                                        fieldWithPath("data.trips[].endDate").attributes(getDateFormat()).description("도착일"),
                                        fieldWithPath("data.trips[].analyzingCount").type(JsonFieldType.NUMBER).description("분석 중 파일 개수"),
                                        fieldWithPath("data.trips[].tripName").type(JsonFieldType.STRING).description("여행 이름")
                                )
                        )
                )
            .andDo(print());
    }

    @Test
    void deleteTrip() throws Exception{
        Mockito.doNothing().when(userService).validateUserWithTrip(any(Long.class), any(Long.class));
        Mockito.when(tripService.delete(any(Long.class))).thenReturn(new Trip());

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/core/trip/{tripId}", 1L)
                .header("userId", 1L)

                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("trip/delete",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("tripId").description("삭제할 여행 ID")
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
    void updateTrip() throws Exception {
        TripRequestDTO.UpdateTrip updateTrip = TripRequestDTO.UpdateTrip.builder()
                .tripId(1L)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .tripName("test")
                .build();
        Mockito.doNothing().when(userService).validateUserWithTrip(any(Long.class), any(Long.class));
        Mockito.doNothing().when(tripService).update(any(Long.class), any(TripRequestDTO.UpdateTrip.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/core/trip")
                .header("userId", 1L)
                .content(Json.pretty(updateTrip))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("trip/update",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestFields(
                                        fieldWithPath("tripId").type(JsonFieldType.NUMBER).description("여행 ID"),
                                        fieldWithPath("startDate").attributes(getDateFormat()).description("출발일"),
                                        fieldWithPath("endDate").attributes(getDateFormat()).description("도착일"),
                                        fieldWithPath("tripName").type(JsonFieldType.STRING).description("여행 이름")
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
    void getTrip() throws Exception {
        TripResponseDTO.GetTripSpec trip = TripResponseDTO.GetTripSpec.builder()
                .id(1L)
                .email("test")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .analyzingCount(0)
                .tripName("test")
                .numOfCard(0)
                .happy(0f)
                .sad(0f)
                .angry(0f)
                .neutral(0f)
                .disgust(0f)
                .build();
        Mockito.doNothing().when(userService).validateUserWithTrip(any(Long.class), any(Long.class));
        Mockito.when(tripService.getTrip(any(Long.class))).thenReturn(trip);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/core/trip/{tripId}", 1L)
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(
                        document("trip/get",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("tripId").description("조회할 여행 ID")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("여행 ID"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("유저 email"),
                                        fieldWithPath("data.startDate").attributes(getDateFormat()).description("출발일"),
                                        fieldWithPath("data.endDate").attributes(getDateFormat()).description("도착일"),
                                        fieldWithPath("data.analyzingCount").type(JsonFieldType.NUMBER).description("분석 중 파일 개수"),
                                        fieldWithPath("data.tripName").type(JsonFieldType.STRING).description("여행 이름"),
                                        fieldWithPath("data.numOfCard").type(JsonFieldType.NUMBER).description("카드뷰 개수"),
                                        fieldWithPath("data.happy").type(JsonFieldType.NUMBER).description("행복한 감정 비율"),
                                        fieldWithPath("data.sad").type(JsonFieldType.NUMBER).description("슬픈 감정 비율"),
                                        fieldWithPath("data.angry").type(JsonFieldType.NUMBER).description("화난 감정 비율"),
                                        fieldWithPath("data.neutral").type(JsonFieldType.NUMBER).description("중립적인 감정 비율"),
                                        fieldWithPath("data.disgust").type(JsonFieldType.NUMBER).description("역겨운 감정 비율")
                                )
                        )
                )
                .andDo(print());
    }
}