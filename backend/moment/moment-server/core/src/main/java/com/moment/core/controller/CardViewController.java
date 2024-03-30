package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.dto.request.CardViewRequestDTO;
import com.moment.core.dto.response.CardViewResponseDTO;
import com.moment.core.service.CardViewService;
import com.moment.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/core/cardView")
public class CardViewController {
    private final CardViewService cardViewService;
    private final UserService userService;

    // 녹음본 업로드
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "녹음본 업로드", description = "녹음본과 데이터를 받아서 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식, 존재하지 않는 유저, 이미 등록된 여행날짜",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<CardViewResponseDTO.GetCardView>> uploadRecord(
            @RequestHeader Long userId,
            @RequestPart CardViewRequestDTO.UploadRecord uploadRecord,
            @RequestPart MultipartFile recordFile
    ) throws IOException {
        CardViewResponseDTO.GetCardView response = cardViewService.uploadRecord(uploadRecord, recordFile, userId);

        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS, response));
    }

    // 카드뷰 전체 가져오기
    @GetMapping("/all/{tripFileId}")
    @Operation(summary = "카드뷰 전체 가져오기", description = "카드뷰 전체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<CardViewResponseDTO.GetAllCardView>> getAllCardView(
            @RequestHeader Long userId,
            @PathVariable Long tripFileId
    ) {
        userService.validateUserWithTripFile(userId, tripFileId);
        CardViewResponseDTO.GetAllCardView allCardView = cardViewService.getAllCardView(userId, tripFileId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, allCardView));
    }

    // 녹음본 수정
    @PutMapping("/{cardViewId}")
    @Operation(summary = "녹음본 수정", description = "녹음본을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식, 존재하지 않는 카드뷰",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> updateRecord(
            @RequestHeader Long userId,
            @PathVariable Long cardViewId,
            @RequestBody CardViewRequestDTO.UpdateRecord updateRecord
    ) {
        userService.validateUserWithCardView(userId, cardViewId);
        cardViewService.updateRecord(cardViewId, updateRecord);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }

    // 녹음본 삭제
    @DeleteMapping("/{cardViewId}")
    @Operation(summary = "녹음본 삭제", description = "녹음본을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식, 존재하지 않는 카드뷰",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> deleteRecord(
            @RequestHeader Long userId,
            @PathVariable Long cardViewId
    ) {
        userService.validateUserWithCardView(userId, cardViewId);
        cardViewService.deleteRecord(cardViewId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.DELETE_SUCCESS));
    }

    // 카드뷰 좋아요
    @PutMapping("/like/{cardViewId}")
    @Operation(summary = "카드뷰 좋아요", description = "카드뷰에 좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식, 존재하지 않는 카드뷰",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> likeCardView(
            @RequestHeader Long userId,
            @PathVariable Long cardViewId
    ) {
        userService.validateUserWithCardView(userId, cardViewId);
        cardViewService.likeCardView(cardViewId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.UPDATE_SUCCESS));
    }

    // 좋아요 누른 카드뷰 조회
    @GetMapping("/like")
    @Operation(summary = "좋아요 누른 카드뷰 조회", description = "좋아요 누른 카드뷰를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<CardViewResponseDTO.GetAllCardView>> getLikeCardView(
            @RequestHeader Long userId
    ) {
        CardViewResponseDTO.GetAllCardView allCardView = cardViewService.getLikeCardView(userId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, allCardView));
    }
}

