package com.moment.auth.controller;

import com.moment.auth.domain.Role;
import com.moment.auth.dto.request.AuthRequest;
import com.moment.auth.dto.response.TokenResponseDTO;
import com.moment.auth.service.AuthService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login() throws Exception {
        TokenResponseDTO.GetToken responseDTO = TokenResponseDTO.GetToken.builder()
                .accessToken("accessToken")
                .refreshToken("refresh")
                .grantType("Bearer")
                .role(Role.ROLE_AUTH_USER)
                .build();

        AuthRequest.Login login = AuthRequest.Login.builder()
                .email("alexj99@naver.com")
                .password("1234")
                .build();

        when(authService.login(any(AuthRequest.Login.class))).thenReturn(responseDTO);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(login)))
                .andExpect(status().isOk())
                .andDo(document("auth/login",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("사용자 권한")
                        )
                ))
                .andDo(print());
    }

    @Test
    void sendCode() throws Exception {
        TokenResponseDTO.GetTempToken tempToken = TokenResponseDTO.GetTempToken.builder()
                .accessToken("accessToken")
                .grantType("Bearer")
                .role(Role.ROLE_AUTH_USER)
                .userId(1L)
                .build();

        AuthRequest.SendCode sendCode = AuthRequest.SendCode.builder()
                .email("alexj99@naver.com")
                .isSignUp("true")
                .build();

        when(authService.sendCode(any(AuthRequest.SendCode.class))).thenReturn(tempToken);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/code")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", 1L)
                .content(Json.pretty(sendCode)))
                .andExpect(status().isOk())
                .andDo(document("auth/sendCode",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("isSignUp").type(JsonFieldType.STRING).description("회원가입 여부")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("사용자 권한"),
                                fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("사용자 ID")
                        )
                ))
                .andDo(print());
    }

    @Test
    void verifyCode() throws Exception {
        TokenResponseDTO.GetToken responseDTO = TokenResponseDTO.GetToken.builder()
                .accessToken("accessToken")
                .refreshToken("refresh")
                .grantType("Bearer")
                .role(Role.ROLE_AUTH_USER)
                .build();
        AuthRequest.VerifyCode verifyCode = AuthRequest.VerifyCode.builder()
                .code("1234")
                .build();

        Mockito.when(authService.verifyCode(any(AuthRequest.VerifyCode.class), any())).thenReturn(responseDTO);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                        .header("userId", 1L)
                .content(Json.pretty(verifyCode)))
                .andExpect(status().isOk())
                .andDo(document("auth/verifyCode",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("userId").description("Bearer Token")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("인증 코드")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                fieldWithPath("data.grantType").type(JsonFieldType.STRING).description("토큰 타입"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).description("리프레시 토큰"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING).description("사용자 권한")
                        )
                ))
                .andDo(print());
    }

    @Test
    void changePassword() throws Exception {
        AuthRequest.ChangePassword changePassword = AuthRequest.ChangePassword.builder()
                .code("1234")
                .newPassword("12345")
                .build();

        Mockito.doNothing().when(authService).changePassword(any(AuthRequest.ChangePassword.class), any());

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/auth/password")
                .header("userId", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.pretty(changePassword)))
                .andExpect(status().isOk())
                .andDo(document("auth/changePassword",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestHeaders(
                                headerWithName("userId").description("Bearer Token")
                        ),
                        requestFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("인증 코드"),
                                fieldWithPath("newPassword").type(JsonFieldType.STRING).description("새로운 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 상태 코드"),
                                fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                                fieldWithPath("msg").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("detailMsg").type(JsonFieldType.STRING).description("상세 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 없음")
                        )
                ))
                .andDo(print());
    }
}