package com.moment.core.controller;

import com.moment.core.dto.request.CardViewRequestDTO;
import com.moment.core.dto.response.CardViewResponseDTO;
import com.moment.core.service.CardViewService;
import com.moment.core.service.ImageFileService;
import com.moment.core.service.UserService;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.moment.core.config.DocumentFormatGenerator.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class CardViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardViewService cardViewService;
    @MockBean
    private UserService userService;
    @MockBean
    private ImageFileService imageFileService;

    @InjectMocks
    private CardViewController cardViewController;

    @Test
    void uploadRecord() throws Exception {
        // given
        Long userId = 1L;
        String jsonStr = "{\n" +
                "  \"location\": \"서울\",\n" +
                "  \"recordedAt\": \"2024-02-08T14:30:00\",\n" +
                "  \"temperature\": \"20\",\n" +
                "  \"weather\": \"맑음\",\n" +
                "  \"question\": \"질문\"\n" +
                "}";
        MockMultipartFile recordFile = new MockMultipartFile("recordFile", "test.mp3", "audio/mp3", "test".getBytes());
        MockMultipartFile uploadRecord = new MockMultipartFile("uploadRecord", "", "application/json", jsonStr.getBytes());
        CardViewResponseDTO.GetCardView mockResponseDto = CardViewResponseDTO.GetCardView.builder()
                .Id(1L)
                .tripFileId(1L)
                .recordFileName("test.mp3")
                .recordFileUrl("http://localhost:8080/")
                .location("서울")
                .recordFileLength(100L)
                .recordedAt(LocalDateTime.parse("2024-02-08T14:30:00"))
                .temperature("20")
                .weather("맑음")
                .stt("test")
                .happy(0.1f)
                .angry(0.1f)
                .neutral(0.1f)
                .disgust(0.1f)
                .isLoved(true)
                .sad(0.1f)
                .recordFileStatus("WAIT")
                .imageUrls(List.of(""))
                .question("질문")
                .build();

        when(cardViewService.uploadRecord(any(CardViewRequestDTO.UploadRecord.class), any(), eq(userId))).thenReturn(mockResponseDto);


        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.multipart("/core/cardView/upload")
                .file(recordFile)
                .file(uploadRecord)
//                        .param("uploadRecord", jsonStr)
                .header("userId", String.valueOf(userId))
                .contentType("multipart/form-data"));

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/uploadRecord",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                requestPartFields("uploadRecord",
                                        fieldWithPath("location").type(JsonFieldType.STRING).description("녹음본 장소"),
                                        fieldWithPath("recordedAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("녹음 시점"),
                                        fieldWithPath("temperature").type(JsonFieldType.STRING).description("온도"),
                                        fieldWithPath("weather").type(JsonFieldType.STRING).description("날씨"),
                                        fieldWithPath("question").type(JsonFieldType.STRING).description("질문")
                                ),
                                requestParts(
                                        partWithName("recordFile").description("녹음 파일"),
                                        partWithName("uploadRecord").description("녹음 정보")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("cardView ID"),
                                        fieldWithPath("data.tripFileId").type(JsonFieldType.NUMBER).description("tripFile ID"),
                                        fieldWithPath("data.recordedAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("녹음 시점"),
                                        fieldWithPath("data.recordFileName").type(JsonFieldType.STRING).description("파일이름"),
                                        fieldWithPath("data.recordFileUrl").type(JsonFieldType.STRING).description("파일 저장 위치"),
                                        fieldWithPath("data.location").type(JsonFieldType.STRING).description("위치"),
                                        fieldWithPath("data.recordFileLength").type(JsonFieldType.NUMBER).description("파일길이"),
                                        fieldWithPath("data.weather").type(JsonFieldType.STRING).description("날씨"),
                                        fieldWithPath("data.temperature").type(JsonFieldType.STRING).description("온도"),
                                        fieldWithPath("data.stt").type(JsonFieldType.STRING).description("STT"),
                                        fieldWithPath("data.happy").type(JsonFieldType.NUMBER).description("기쁨"),
                                        fieldWithPath("data.sad").type(JsonFieldType.NUMBER).description("슬픔"),
                                        fieldWithPath("data.angry").type(JsonFieldType.NUMBER).description("화남"),
                                        fieldWithPath("data.neutral").type(JsonFieldType.NUMBER).description("중립"),
                                        fieldWithPath("data.disgust").type(JsonFieldType.NUMBER).description("혐오"),
                                        fieldWithPath("data.question").type(JsonFieldType.STRING).description("질문"),
                                        fieldWithPath("data.loved").type(JsonFieldType.BOOLEAN).description("즐겨찾기"),
                                        fieldWithPath("data.recordFileStatus").type(JsonFieldType.STRING).description("분석 상태"),
                                        fieldWithPath("data.imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL")
                                )
                        )
                )
        .andDo(print());
        // then
    }

    @Test
    void getAllCardView() throws Exception {
        CardViewResponseDTO.GetCardView mockResponseDto1 = CardViewResponseDTO.GetCardView.builder()
                .Id(1L)
                .tripFileId(1L)
                .recordFileName("test.mp3")
                .recordFileUrl("http://localhost:8080/")
                .location("서울")
                .recordFileLength(100L)
                .recordedAt(LocalDateTime.parse("2024-02-08T14:30:00"))
                .temperature("20")
                .weather("맑음")
                .stt("test")
                .happy(0.1f)
                .angry(0.1f)
                .neutral(0.1f)
                .disgust(0.1f)
                .isLoved(true)
                .sad(0.1f)
                .recordFileStatus("WAIT")
                .question("질문")
                .imageUrls(List.of(""))
                .build();

        CardViewResponseDTO.GetCardView mockResponseDto2 = CardViewResponseDTO.GetCardView.builder()
                .Id(2L)
                .tripFileId(1L)
                .recordFileName("test.mp3")
                .recordFileUrl("http://localhost:8080/")
                .location("서울")
                .recordFileLength(100L)
                .recordedAt(LocalDateTime.parse("2024-02-08T15:30:00"))
                .temperature("20")
                .weather("맑음")
                .stt("test")
                .happy(0.1f)
                .angry(0.1f)
                .neutral(0.1f)
                .disgust(0.1f)
                .isLoved(false)
                .sad(0.1f)
                .recordFileStatus("WAIT")
                .question("질문")
                .imageUrls(List.of(""))
                .build();

        List<CardViewResponseDTO.GetCardView> cardViews = List.of(mockResponseDto1, mockResponseDto2);
        CardViewResponseDTO.GetAllCardView mockResponseDto = CardViewResponseDTO.GetAllCardView.builder()
                .cardViews(cardViews)
                .build();

        Mockito.doNothing().when(userService).validateUserWithTripFile(any(), any());
        when(cardViewService.getAllCardView(any(), any())).thenReturn(mockResponseDto);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/core/cardView/all/{tripFileId}", 1L)
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/getAllCardView",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("tripFileId").description("여행 파일 ID")
                                ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data.cardViews[].id").type(JsonFieldType.NUMBER).description("cardView ID"),
                                        fieldWithPath("data.cardViews[].tripFileId").type(JsonFieldType.NUMBER).description("tripFile ID"),
                                        fieldWithPath("data.cardViews[].recordedAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("녹음 시점"),
                                        fieldWithPath("data.cardViews[].recordFileName").type(JsonFieldType.STRING).description("파일이름"),
                                        fieldWithPath("data.cardViews[].recordFileUrl").type(JsonFieldType.STRING).description("파일 저장 위치"),
                                        fieldWithPath("data.cardViews[].location").type(JsonFieldType.STRING).description("위치"),
                                        fieldWithPath("data.cardViews[].recordFileLength").type(JsonFieldType.NUMBER).description("파일길이"),
                                        fieldWithPath("data.cardViews[].weather").type(JsonFieldType.STRING).description("날씨"),
                                        fieldWithPath("data.cardViews[].temperature").type(JsonFieldType.STRING).description("온도"),
                                        fieldWithPath("data.cardViews[].stt").type(JsonFieldType.STRING).description("STT"),
                                        fieldWithPath("data.cardViews[].happy").type(JsonFieldType.NUMBER).description("기쁨"),
                                        fieldWithPath("data.cardViews[].sad").type(JsonFieldType.NUMBER).description("슬픔"),
                                        fieldWithPath("data.cardViews[].angry").type(JsonFieldType.NUMBER).description("화남"),
                                        fieldWithPath("data.cardViews[].neutral").type(JsonFieldType.NUMBER).description("중립"),
                                        fieldWithPath("data.cardViews[].disgust").type(JsonFieldType.NUMBER).description("혐오"),
                                        fieldWithPath("data.cardViews[].question").type(JsonFieldType.STRING).description("질문"),
                                        fieldWithPath("data.cardViews[].loved").type(JsonFieldType.BOOLEAN).description("즐겨찾기"),
                                        fieldWithPath("data.cardViews[].recordFileStatus").type(JsonFieldType.STRING).description("분석 상태"),
                                        fieldWithPath("data.cardViews[].imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL")
                                )
                        )
                )
                .andDo(print());
    }

    @Test
    void updateRecord() throws Exception {
        Mockito.doNothing().when(userService).validateUserWithCardView(any(), any());
        Mockito.doNothing().when(cardViewService).updateRecord(any(Long.class), any(CardViewRequestDTO.UpdateRecord.class));

        CardViewRequestDTO.UpdateRecord updateRecord = CardViewRequestDTO.UpdateRecord.builder()
                .location("서울")
                .stt("test")
                .temperature("20")
                .weather("맑음")
                .question("질문")
                .build();

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/core/cardView/{cardViewId}", 1L)
                .header("userId", "1")
                .contentType("application/json")
                .content(Json.pretty(updateRecord))
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/updateRecord",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("cardViewId").description("카드뷰 ID")
                                ),
                                requestFields(
                                        fieldWithPath("location").type(JsonFieldType.STRING).description("녹음본 장소"),
                                        fieldWithPath("weather").type(JsonFieldType.STRING).description("날씨"),
                                        fieldWithPath("temperature").type(JsonFieldType.STRING).description("온도"),
                                        fieldWithPath("question").type(JsonFieldType.STRING).description("질문"),
                                        fieldWithPath("stt").type(JsonFieldType.STRING).description("STT")
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
    void deleteRecord() throws Exception {
        Mockito.doNothing().when(userService).validateUserWithCardView(any(), any());
        Mockito.doNothing().when(cardViewService).deleteRecord(any(Long.class));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/core/cardView/{cardViewId}", 1L)
                .header("userId", "1")
                .contentType("application/json")
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/deleteRecord",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("cardViewId").description("카드뷰 ID")
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
    void likeCardView() throws Exception {
        Mockito.doNothing().when(userService).validateUserWithCardView(any(), any());
        Mockito.doNothing().when(cardViewService).likeCardView(any(Long.class));

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/core/cardView/like/{cardViewId}", 1L)
                .header("userId", "1")
                .contentType("application/json")
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/likeCardView",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("userId").description("Bearer Token")
                                ),
                                pathParameters(
                                        parameterWithName("cardViewId").description("카드뷰 ID")
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
    void getLikeCardView() throws Exception{
        CardViewResponseDTO.GetCardView mockResponseDto1 = CardViewResponseDTO.GetCardView.builder()
                .Id(1L)
                .tripFileId(1L)
                .recordFileName("test.mp3")
                .recordFileUrl("http://localhost:8080/")
                .location("서울")
                .recordFileLength(100L)
                .recordedAt(LocalDateTime.parse("2024-02-08T14:30:00"))
                .temperature("20")
                .weather("맑음")
                .stt("test")
                .happy(0.1f)
                .angry(0.1f)
                .neutral(0.1f)
                .disgust(0.1f)
                .isLoved(true)
                .sad(0.1f)
                .recordFileStatus("WAIT")
                .question("질문")
                .imageUrls(List.of(""))
                .build();

        CardViewResponseDTO.GetCardView mockResponseDto2 = CardViewResponseDTO.GetCardView.builder()
                .Id(2L)
                .tripFileId(1L)
                .recordFileName("test.mp3")
                .recordFileUrl("http://localhost:8080/")
                .location("서울")
                .recordFileLength(100L)
                .recordedAt(LocalDateTime.parse("2024-02-08T15:30:00"))
                .temperature("20")
                .weather("맑음")
                .stt("test")
                .happy(0.1f)
                .angry(0.1f)
                .neutral(0.1f)
                .disgust(0.1f)
                .isLoved(false)
                .sad(0.1f)
                .recordFileStatus("WAIT")
                .question("질문")
                .imageUrls(List.of(""))
                .build();

        List<CardViewResponseDTO.GetCardView> cardViews = List.of(mockResponseDto1, mockResponseDto2);
        CardViewResponseDTO.GetAllCardView mockResponseDto = CardViewResponseDTO.GetAllCardView.builder()
                .cardViews(cardViews)
                .build();

        when(cardViewService.getLikeCardView(any(Long.class))).thenReturn(mockResponseDto);

        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/core/cardView/like")
                .header("userId", "1")
                .contentType("application/json")
        );

        result.andExpect(status().isOk())
                .andDo(
                        document("cardView/getLikeCardView",
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
                                        fieldWithPath("data.cardViews[].id").type(JsonFieldType.NUMBER).description("cardView ID"),
                                        fieldWithPath("data.cardViews[].tripFileId").type(JsonFieldType.NUMBER).description("tripFile ID"),
                                        fieldWithPath("data.cardViews[].recordedAt").type(JsonFieldType.STRING).attributes(getDateTimeFormat()).description("녹음 시점"),
                                        fieldWithPath("data.cardViews[].recordFileName").type(JsonFieldType.STRING).description("파일이름"),
                                        fieldWithPath("data.cardViews[].recordFileUrl").type(JsonFieldType.STRING).description("파일 저장 위치"),
                                        fieldWithPath("data.cardViews[].location").type(JsonFieldType.STRING).description("위치"),
                                        fieldWithPath("data.cardViews[].recordFileLength").type(JsonFieldType.NUMBER).description("파일길이"),
                                        fieldWithPath("data.cardViews[].weather").type(JsonFieldType.STRING).description("날씨"),
                                        fieldWithPath("data.cardViews[].temperature").type(JsonFieldType.STRING).description("온도"),
                                        fieldWithPath("data.cardViews[].stt").type(JsonFieldType.STRING).description("STT"),
                                        fieldWithPath("data.cardViews[].happy").type(JsonFieldType.NUMBER).description("기쁨"),
                                        fieldWithPath("data.cardViews[].sad").type(JsonFieldType.NUMBER).description("슬픔"),
                                        fieldWithPath("data.cardViews[].angry").type(JsonFieldType.NUMBER).description("화남"),
                                        fieldWithPath("data.cardViews[].neutral").type(JsonFieldType.NUMBER).description("중립"),
                                        fieldWithPath("data.cardViews[].disgust").type(JsonFieldType.NUMBER).description("혐오"),
                                        fieldWithPath("data.cardViews[].question").type(JsonFieldType.STRING).description("질문"),
                                        fieldWithPath("data.cardViews[].loved").type(JsonFieldType.BOOLEAN).description("즐겨찾기"),
                                        fieldWithPath("data.cardViews[].recordFileStatus").type(JsonFieldType.STRING).description("분석 상태"),
                                        fieldWithPath("data.cardViews[].imageUrls").type(JsonFieldType.ARRAY).description("이미지 URL")
                                )
                        )
                )
                .andDo(print());
    }

//    @Test
//    void deleteImages
}