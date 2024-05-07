package com.moment.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moment.core.dto.request.UserRequestDTO;
import com.moment.core.service.UserService;
import io.swagger.v3.core.util.Json;
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

import static com.moment.core.config.DocumentFormatGenerator.getDateFormat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void updateUserSetting() throws Exception {
        UserRequestDTO.updateUser request = UserRequestDTO.updateUser.builder()
                .firebaseToken("firebaseToken")
                .notification(true)
                .dataUsage(true)
                .build();
        Long userId = 1L;

        Mockito.doNothing().when(userService).updateUserSetting(request, userId);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/core/user/setting")
                .header("userId", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(request)))
                .andExpect(status().isOk())
                .andDo(document("user/setting",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("userId").description("Bearer Token")
                        ),
                        requestFields(
                                fieldWithPath("firebaseToken").type(JsonFieldType.STRING).description("firebaseToken"),
                                fieldWithPath("notification").type(JsonFieldType.BOOLEAN).description("notification"),
                                fieldWithPath("dataUsage").type(JsonFieldType.BOOLEAN).description("dataUsage")
                        ),
                                responseFields(
                                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                        fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                        fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터")
                                )
                        )
                )
                .andDo(print());
    }
}