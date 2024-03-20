package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.dto.request.TripRequestDTO;
import com.moment.core.dto.response.TripResponseDTO;
import com.moment.core.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/core/trip")
public class TripController {
    private final TripService tripService;

    // 여행 등록
    @PostMapping("/register")
    @Operation(summary = "여행 등록", description = "여행을 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식, 존재하지 않는 유저, 이미 등록된 여행날짜",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse> registerTrip(
            @RequestBody TripRequestDTO.RegisterTrip registerTrip
    ) {
        tripService.register(registerTrip);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.INSERT_SUCCESS));
    }

    // 모든 여행 가져오기
    @GetMapping("/all")
    @Operation(summary = "모든 여행 가져오기", description = "모든 여행을 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<TripResponseDTO.GetAllTrip>> getAllTrip(
            @RequestParam Long userId
    ) {
        TripResponseDTO.GetAllTrip allTrip = tripService.getAllTrip(userId);
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, allTrip));
    }
}
