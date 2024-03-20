package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.dto.request.CardViewRequestDTO;
import com.moment.core.dto.response.CardViewResponseDTO;
import com.moment.core.service.CardViewService;
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
        CardViewResponseDTO.GetAllCardView allCardView = cardViewService.getAllCardView(userId, tripFileId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, allCardView));
    }
}

