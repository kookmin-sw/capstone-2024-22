package com.moment.core.controller;

import com.moment.core.common.APIResponse;
import com.moment.core.common.code.SuccessCode;
import com.moment.core.dto.response.AlreadyBookedDateResponseDTO;
import com.moment.core.service.AlreadyBookedDateService;
import com.moment.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/core/alreadyBookedDate")
public class AlreadyBookedDateController {
    private final AlreadyBookedDateService alreadyBookedDateService;
    private final UserService userService;

    // 예약일 전부 가져오기
    @GetMapping("/all")
    @Operation(summary = "기예약일 전체 가져오기", description = "기예약일 전체를 가져옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "옳바르지 않은 요청 방식",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    public ResponseEntity<APIResponse<AlreadyBookedDateResponseDTO.GetAllCardView>> getAll(
            @RequestHeader Long userId
    ) {
        return ResponseEntity.ok(APIResponse.of(SuccessCode.SELECT_SUCCESS, alreadyBookedDateService.getAll(userId)));
    }
}
